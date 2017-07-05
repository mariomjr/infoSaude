package br.ufg.inf.infosaude.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.model.Especialidades;

public class ListHospitalAdapter extends RecyclerView.Adapter<ListHospitalAdapter.ViewHolder> {

    private final Context context;
    private final List<Especialidades> especialidades;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_hospitais_list, parent,  false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Especialidades especialidades = this.especialidades.get(position);
        holder.tvEspecialidade.setText(especialidades.getNome());
        holder.tvQuantidade.setText(""+especialidades.getQuantidade());

    }

    @Override
    public int getItemCount() {
        return especialidades.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder {

        protected TextView tvEspecialidade;
        protected TextView tvQuantidade;

        public ViewHolder(View itemView) {
            super(itemView);

            tvEspecialidade = (TextView) itemView.findViewById(R.id.especialidade);
            tvQuantidade = (TextView) itemView.findViewById(R.id.quantidade);
        }
    }

    public ListHospitalAdapter(Context context, List<Especialidades> especialidades){

        this.context = context;
        this.especialidades = especialidades;
    }



}
