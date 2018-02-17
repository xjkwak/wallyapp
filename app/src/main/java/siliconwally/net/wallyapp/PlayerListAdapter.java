package siliconwally.net.wallyapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerListViewHolder> {

    private List<Player> list;
    int selectedPosition = 0;
    private Context context;
    private Map<String, Player> selectedPlayers;

    public PlayerListAdapter(List<Player> lista, Context context) {
        this.list = lista;
        this.context = context;
        this.selectedPlayers = new HashMap<>();
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
        Picasso.with(context).load("http://siliconwally.net/"+player.getPhoto()).into(holder.photo);

        holder.number.setBackgroundColor(selectedPosition == position ? Color.GREEN: Color.TRANSPARENT);

        if (holder.enable.isChecked()) {
            this.selectedPlayers.put(player.getName(), player);
        }
        else {
            this.selectedPlayers.remove(player.getName());
        }
        System.out.println("mapa:");
        System.out.println(this.selectedPlayers);

    }

    @Override
    public int getItemCount() {
      return this.list.size();
    }

    public Map<String, Player> getSelectedPlayers() {

        return this.selectedPlayers;
    }

    public class PlayerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView number;
        private ImageView photo;
        private Switch enable;

        public PlayerListViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            number =( TextView) itemView.findViewById(R.id.number);
            photo = (ImageView)itemView.findViewById(R.id.photo);
            enable = (Switch) itemView.findViewById(R.id.enable);
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

        if (isChecked) {
            this.getSelectedPlayers().put(String.valueOf(player.getNid()), player);
        }
        else {
            this.getSelectedPlayers().remove(player.getNid());
        }
    }
}
