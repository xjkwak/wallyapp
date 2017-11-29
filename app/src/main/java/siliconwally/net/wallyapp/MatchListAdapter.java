package siliconwally.net.wallyapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

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
        holder.txtTime.setText(match.getTime());
        holder.txtTeams.setText(match.getTeams());
    }

    @Override
    public int getItemCount() {
      return this.list.size();
    }

    public class MatchListViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTime;
        private TextView txtTeams;
        private Button arbitrar;
        private Button ver;

        public MatchListViewHolder(View itemView) {
            super(itemView);
            txtTime = (TextView) itemView.findViewById(R.id.match_time);
            txtTeams =( TextView) itemView.findViewById(R.id.match_teams);
            arbitrar = itemView.findViewById(R.id.arbitrar);
            ver = itemView.findViewById(R.id.ver);

            arbitrar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    selectedPosition = getAdapterPosition();
                    Match match = list.get(selectedPosition);
                    MatchListAdapter.this.showScoreboard(match);
                }
            });

            ver.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    selectedPosition = getAdapterPosition();
                    Match match = list.get(selectedPosition);
                    MatchListAdapter.this.showScoreboardViewer(match);
                }
            });
        }
    }

    private void showScoreboard(Match match) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("match", match);
        context.startActivity(intent);
    }

    private void showScoreboardViewer(Match match) {
        Intent intent = new Intent(context, ScoreboardActivity.class);
        intent.putExtra("match", match);
        context.startActivity(intent);
    }
}
