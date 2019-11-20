package com.trab.myapplication.Adapter;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trab.myapplication.Model.Post;
import com.trab.myapplication.Model.PostDAO;
import com.trab.myapplication.Model.User;
import com.trab.myapplication.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<Post> posts;
    private ArrayList<User> users;
    private ArrayList<Boolean> likeds;
    private int currentUserId;
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        return new PostViewHolder(postitem);
    }

    public PostAdapter(ArrayList<Post> postagens, ArrayList<User> users,ArrayList<Boolean> likeds, int currentUserId) {
        this.posts = postagens;
        this.users = users;
        this.likeds = likeds;
        this.currentUserId = currentUserId;
        Log.w("Teste","Adapter definido");
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        Log.w("Teste","chegou no viewholder binder " +position);
        Post post = posts.get((posts.size() - position)-1);
        holder.user.setImageBitmap(BitmapFactory.decodeByteArray(users.get((posts.size() - position)-1).imagesource,
                0,users.get((posts.size() - position)-1).imagesource.length));
        if(posts.get((posts.size() - position)-1).imagesource==null){
            holder.image.setVisibility(View.GONE);
        }else {
            holder.image.setImageBitmap(BitmapFactory.decodeByteArray(posts.get((posts.size() - position)-1).imagesource,
                    0,posts.get((posts.size() - position)-1).imagesource.length));
        }
        holder.username.setText(users.get((posts.size() - position)-1).nome);
        holder.description.setText(posts.get((posts.size() - position)-1).descricao);
        holder.datetime.setText(posts.get((posts.size() - position)-1).datapublicacao.toString());
        /*holder.like = changethemes(likeds.get(position),holder.like);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.like=changethemes(likeds.get(position),holder.like);
                PostDAO postDAO = new PostDAO(view.getContext());
                postDAO.Like(currentUserId,posts.get(position).id);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        private TextView username;
        private TextView datetime;
        private TextView description;
        private ImageView image;
        private Button like;
        private ImageView user;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.w("Teste","chegou no viewholder");
            username = (TextView) itemView.findViewById(R.id.usernamefield);
            datetime = (TextView) itemView.findViewById(R.id.datetimefield);
            description = (TextView) itemView.findViewById(R.id.descricaofield);
            image = (ImageView) itemView.findViewById(R.id.postimage);
            user = (ImageView) itemView.findViewById(R.id.userimage);
            like = (Button) itemView.findViewById(R.id.likebtn);
        }
    }
    /*public Button changethemes(boolean whichone,Button change){
        if(whichone){
            change.setBackgroundColor(change.getContext().getResources().getColor(R.color.toolbar));
            Drawable img = change.getContext().getResources().getDrawable( R.drawable.ic_favorite_black_24dp );
            img.setBounds( 0, 0, 60, 60 );
            change.setCompoundDrawables( img, null, null, null );
            change.setTextColor(change.getContext().getResources().getColor(R.color.toolbar));
        }else{
            change.setBackgroundColor(change.getContext().getResources().getColor(R.color.branco));
            Drawable img = change.getContext().getResources().getDrawable( R.drawable.ic_favorite_border_black_24dp );
            img.setBounds( 0, 0, 60, 60 );
            change.setCompoundDrawables( img, null, null, null );
            change.setTextColor(change.getContext().getResources().getColor(R.color.branco));
        }
        return change;
    }*/
}
