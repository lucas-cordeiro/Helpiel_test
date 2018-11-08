package cordeiro.lucas.helpie.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cordeiro.lucas.helpie.R;
import cordeiro.lucas.helpie.model.User;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyViewHolder>{

    private List<User> users;

    public AdapterUser(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_user, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        User user = users.get(i);
        myViewHolder.txtId.setText(String.valueOf(user.getId()));
        myViewHolder.txtName.setText(user.getName());
        myViewHolder.txtEmail.setText(String.valueOf("Email: "+user.getEmail()));
        myViewHolder.txtCompanyName.setText(String.valueOf("Company Name: "+user.getCompanyName()));
        myViewHolder.txtCity.setText(String.valueOf("City: "+user.getCity()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    protected class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txtId;
        private TextView txtName;
        private TextView txtEmail;
        private TextView txtCompanyName;
        private TextView txtCity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtCompanyName = itemView.findViewById(R.id.txtCompanyName);
            txtCity = itemView.findViewById(R.id.txtCity);
        }
    }
}
