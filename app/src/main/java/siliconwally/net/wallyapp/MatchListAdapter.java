package siliconwally.net.wallyapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;



public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MatchListViewHolder> {

    private List<Match> list;
    int selectedPosition = 0;
    private Context context;
    private String userUid;

    public MatchListAdapter(Context context, List<Match> lista, String userUid) {
        this.context = context;
        this.list = lista;
        this.userUid = userUid;
    }

    public List<Match> getList() {
        return list;
    }

    public void setList(List<Match> list) {
        this.list = list;
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

        if (!match.getUidArbitro().equals(this.userUid)) {
            holder.arbitrar.setVisibility(View.INVISIBLE);
        }
        else {
            holder.arbitrar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
      return this.list.size();
    }

    public class MatchListViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTime;
        private TextView txtTeams;
        private ImageView arbitrar;
        //private Button ver;

        public MatchListViewHolder(View itemView) {
            super(itemView);
            txtTime = (TextView) itemView.findViewById(R.id.match_time);
            txtTeams =( TextView) itemView.findViewById(R.id.match_teams);
            arbitrar = (ImageView) itemView.findViewById(R.id.arbitrar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = getAdapterPosition();
                    Match match = list.get(selectedPosition);
                    MatchListAdapter.this.showScoreboardViewer(match);
                }
            });
        }
    }

    private void showScoreboardViewer(Match match) {
        Intent intent = new Intent(context, DetailTeamActivity.class);
        intent.putExtra("match", match);
        intent.putExtra("userId", this.userUid);
        context.startActivity(intent);
    }
}
