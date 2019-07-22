package org.superbiz.moviefun;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    private final PlatformTransactionManager moviePlatformTransactionManager;
    private final PlatformTransactionManager albumPlatformTransactionManager;

    public HomeController(MoviesBean moviesBean, AlbumsBean albumsBean, MovieFixtures movieFixtures, AlbumFixtures albumFixtures,
                          PlatformTransactionManager moviePlatformTransactionManager, PlatformTransactionManager albumPlatformTransactionManager) {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
        this.albumPlatformTransactionManager = albumPlatformTransactionManager;
        this.moviePlatformTransactionManager = moviePlatformTransactionManager;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {

        TransactionTemplate movieTransactionTemplate = new TransactionTemplate(moviePlatformTransactionManager);

        /*DefaultTransactionDefinition movieDef = new DefaultTransactionDefinition();
        movieDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus movieTransactionStatus = moviePlatformTransactionManager.getTransaction(movieDef);
        try{*/
        movieTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                for (Movie movie : movieFixtures.load()) {

                    moviesBean.addMovie(movie);
                }
            }
        });

        TransactionTemplate albumTransactionTemplate = new TransactionTemplate(albumPlatformTransactionManager);
        albumTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                for (Album album : albumFixtures.load()) {
                    albumsBean.addAlbum(album);
                }
            }
        });

        /*}
        catch(Exception ex){
            moviePlatformTransactionManager.rollback(movieTransactionStatus);
            throw ex;
        }

        moviePlatformTransactionManager.commit(movieTransactionStatus);


        DefaultTransactionDefinition albumDef = new DefaultTransactionDefinition();
        albumDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus albumTransactionStatus = albumPlatformTransactionManager.getTransaction(albumDef);
        try{
        }
        catch(Exception ex){
            albumPlatformTransactionManager.rollback(albumTransactionStatus);
            throw ex;
        }

        albumPlatformTransactionManager.commit(albumTransactionStatus);*/


        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());

        return "setup";
    }
}
