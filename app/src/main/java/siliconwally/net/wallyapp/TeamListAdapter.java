package siliconwally.net.wallyapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamListViewHolder> {

    private List<Team> list;
    int selectedPosition = 0;

    public TeamListAdapter(List<Team> lista) {
        this.list = lista;
    }

    @Override
    public TeamListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_row, parent, false);

        return new TeamListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TeamListViewHolder holder, int position) {
        final Team team = list.get(position);
        holder.name.setText(team.getName());
        holder.company.setText(team.getCompany());

        holder.company.setBackgroundColor(selectedPosition == position ? Color.GREEN: Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
      return this.list.size();
    }

    public class TeamListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView company;

        public TeamListViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            company =( TextView) itemView.findViewById(R.id.company);
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
