package cordeiro.lucas.helpie.api;

import java.util.List;

import cordeiro.lucas.helpie.model.User;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {
    @GET("users/")
    Call<List<User>> recuperarUsers();
}
