package cordeiro.lucas.helpie.api;

import java.util.List;

import cordeiro.lucas.helpie.model.Photo;
import cordeiro.lucas.helpie.model.Post;
import cordeiro.lucas.helpie.model.User;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataService {
    @GET("users/")
    Observable<List<User>> recuperarUsersObservable();

    @GET("posts")
    Call<List<Post>> recuperarPosts(@Query("userId") String userId);

    @GET("photos/")
    Call<List<Photo>> recuperarPhotos( );

}
