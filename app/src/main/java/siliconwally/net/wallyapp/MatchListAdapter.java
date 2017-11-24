package siliconwally.net.wallyapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MatchListViewHolder> {

    private List<Match> list;
    int selectedPosition = 0;
    private Context context;

    public MatchListAdapter(Context context, List<Match> lista) {
        this.context = context;
        this.list = lista;
    }

    @Override
    public MatchListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_row, parent, false);

        return new MatchListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MatchListViewHolder holder, int position) {
        final Match match = list.get(position);
        holder.txtDate.setText(match.getDate());
        holder.txtTeams.setText(match.getTeams());

        holder.txtTeams.setBackgroundColor(selectedPosition == position ? Color.GREEN: Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
      return this.list.size();
    }

    public class MatchListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDate;
        private TextView txtTeams;

        public MatchListViewHolder(View itemView) {
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

            Match match = list.get(selectedPosition);
            System.out.println("Seleccionado: " + match);
            MatchListAdapter.this.showScoreboard(match);
            // Do your another stuff for your onClick
        }
    }

    private void showScoreboard(Match match) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("match", match);
        context.startActivity(intent);

    }
}
