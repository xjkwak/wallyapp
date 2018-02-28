package siliconwally.net.wallyapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import siliconwally.net.wallyapp.service.ConstantsRestApi;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerListViewHolder> {

    private List<Player> list;
    private int selectedPosition = 0;
    private Context context;

    public PlayerListAdapter(List<Player> lista, Context context) {
        this.list = lista;
        this.context = context;
    }

    @Override
    public PlayerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_row, parent, false);

        return new PlayerListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlayerListViewHolder holder, int position) {
        final Player player = list.get(position);
        holder.name.setText(player.getName());
        holder.number.setText(player.getNumber());
        Picasso.with(context).load(ConstantsRestApi.BASE_URL + "/" + player.getPhoto()).into(holder.photo);
        holder.enable.setChecked(player.isEnabled());

        holder.number.setBackgroundColor(selectedPosition == position
                ? ContextCompat.getColor(context, R.color.colorAccent)
                : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
      return this.list.size();
    }

    public class PlayerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView number;
        private ImageView photo;
        private Switch enable;

        public PlayerListViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            photo = itemView.findViewById(R.id.photo);
            enable = itemView.findViewById(R.id.enable);
            enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PlayerListAdapter.this.addSelectedPlayer(getAdapterPosition(), isChecked);
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            notifyItemChanged(selectedPosition);
            selectedPosition = getAdapterPosition();
            notifyItemChanged(selectedPosition);
            // Do your another stuff for your onClick
        }
    }

    private void addSelectedPlayer(int adapterPosition, boolean isChecked) {
        Player player = this.list.get(adapterPosition);
        player.setEnabled(isChecked);
    }
}