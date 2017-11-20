package siliconwally.net.wallyapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.UserListViewHolder> {

    private ArrayList<Match> list;
    int selectedPosition = 0;

    public MatchListAdapter(ArrayList<Match> lista) {
        this.list = lista;
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_row, parent, false);

        return new MatchListAdapter.UserListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        final Match match = list.get(position);
        holder.txtDate.setText(android.text.format.DateFormat.getDateFormat(getApplicationContext()).format(match.getDate()));
        holder.txtTeams.setText(match.getTeams());

        holder.txtTeams.setBackgroundColor(selectedPosition == position ? Color.GREEN: Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
      return this.list.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDate;
        private TextView txtTeams;

        public UserListViewHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtTeams =( TextView) itemView.findViewById(R.id.txt_teams);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println("Entra: ");
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;


            // Updating old as well as new positions
            notifyItemChanged(selectedPosition);
            selectedPosition = getAdapterPosition();
            System.out.println("Entra: " + selectedPosition);
            notifyItemChanged(selectedPosition);
            // Do your another stuff for your onClick
        }
    }
}
