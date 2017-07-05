package br.ufg.inf.infosaude.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.ufg.inf.infosaude.R;
import br.ufg.inf.infosaude.model.Favorito;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Favorito> favorito;

    public DataAdapter(List<Favorito> favorito) {
        this.favorito = favorito;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_nome.setText(favorito.get(i).getNomeHospital());
    }

    @Override
    public int getItemCount() {
        return favorito.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nome;

        public ViewHolder(View view) {
            super(view);

            tv_nome = (TextView) view.findViewById(R.id.tv_nome);
        }
    }
}
