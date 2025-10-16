package adapters;

import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.useful.nfcclient.R;
import ca.useful.nfcclient.databinding.ItemTwoTextviewsBinding;
import cache.BaseCache;
import connector.CacheDisplayer;

public class WifiP2PRecyclerAdapter extends RecyclerView.Adapter<WifiP2PRecyclerAdapter.ViewHolder> {
    private List<WifiP2pDevice> allServiceInfos;
    private List<WifiP2pDevice> filteredServiceInfos;

    public interface WifiP2PClickedIF {
        void onClick(WifiP2pDevice device);
    }
    private String query = null;
    private WifiP2PClickedIF wifiP2PClickedIF;
    public WifiP2PRecyclerAdapter(List<WifiP2pDevice> allServiceInfos,
                                  WifiP2PClickedIF wifiP2PClickedIF) {
        this.allServiceInfos = allServiceInfos;
        this.filteredServiceInfos = allServiceInfos;
        this.wifiP2PClickedIF = wifiP2PClickedIF;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_two_textviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WifiP2pDevice device = filteredServiceInfos.get(holder.getBindingAdapterPosition());
        holder.binding.text1.setText(device.deviceName);
        holder.binding.text2.setText(device.deviceAddress + ";" + device.primaryDeviceType);
        holder.binding.getRoot().setOnClickListener(v -> {
            wifiP2PClickedIF.onClick(filteredServiceInfos.get(holder.getBindingAdapterPosition()));
        });
    }


    public void doFilter(String query) {
        this.query = query != null ? query : "";
        filteredServiceInfos = new ArrayList<>();
        String[] queries = this.query.split(" ");
        if (queries.length == 0 || queries[0].equals("")) {
            filteredServiceInfos = new ArrayList<>(allServiceInfos);
            notifyDataSetChanged();
            return;
        }

        for (WifiP2pDevice serviceInfo : allServiceInfos) {
            StringBuilder sb = new StringBuilder();
            sb.append(serviceInfo.deviceName);
            sb.append(serviceInfo.deviceAddress);
            sb.append(String.valueOf(serviceInfo.primaryDeviceType));

            boolean[] present = new boolean[queries.length];
            for (int i = 0; i < queries.length; i++) {
                String q = queries[i];
                if (sb.toString().toLowerCase().contains(q.toLowerCase())) {
                    present[i] = true;
                } else {
                    present[i] = false;
                }
            }
            boolean matches = true;
            for (int i = 0; i < present.length; i++) {
                if (!present[i]) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                filteredServiceInfos.add(serviceInfo);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredServiceInfos.size();
    }
    public void refreshItems(List<WifiP2pDevice> devices) {
        if (!allServiceInfos.equals(devices)) {
            this.allServiceInfos = new ArrayList<>(devices);
            doFilter(query);
        }
    }

    public void addItem(WifiP2pDevice device) {
        allServiceInfos.add(device);
        notifyDataSetChanged();
    }


    public List<WifiP2pDevice> getAllItems() {
        return allServiceInfos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTwoTextviewsBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTwoTextviewsBinding.bind(itemView);
        }
    }
}
