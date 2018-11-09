package cordeiro.lucas.helpie.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.model.Post;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder>{

    private List<Post> posts;

    public AdapterPost(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_post, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Post post = posts.get(i);
        myViewHolder.txtId.setText(String.valueOf(post.getId()));
        myViewHolder.txtTitle.setText(post.getTitle());
        myViewHolder.txtBody.setText(post.getBody());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txtId;
        private TextView txtTitle;
        private TextView txtBody;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtIdPost);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtBody = itemView.findViewById(R.id.txtBody);
        }
    }
}
