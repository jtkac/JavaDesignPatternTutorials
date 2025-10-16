package adapters;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import ca.useful.nfcclient.ExampleActivityI;
import ca.useful.nfcclient.R;
import ca.useful.nfcclient.databinding.ItemWifiDirectActiveBinding;
import connector.ConnectorExampleI;

public class WifiDirectServerAdapter extends RecyclerView.Adapter<WifiDirectServerAdapter.ViewHolder> {
    private ConnectorExampleI connectorExampleI;
    private Map<Long, WifiP2pDevice> deviceMap = new HashMap<>();
    private Map<Long, WifiP2pConfig> deviceConfigMap = new HashMap<>();

    public WifiDirectServerAdapter(ConnectorExampleI connectorExampleI, Map<Long, WifiP2pDevice> deviceMap) {
        this.connectorExampleI = connectorExampleI;
        this.deviceMap = deviceMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_direct_active, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WifiP2pDevice device = null;
        WifiP2pConfig config = null;
        int pos = 0;
        for (Map.Entry<Long, WifiP2pDevice> entry : deviceMap.entrySet()) {
            if (holder.getBindingAdapterPosition() == pos) {
                device = entry.getValue();
                break;
            }
            pos += 1;
        }
        pos = 0;
        for (Map.Entry<Long, WifiP2pConfig> entry : deviceConfigMap.entrySet()) {
            if (holder.getBindingAdapterPosition() == pos) {
                config = entry.getValue();
                break;
            }
            pos += 1;
        }
        if (device != null) {
            holder.binding.iwdaDeviceName.setText(device.deviceName);
            holder.binding.iwdaDevicePin.setText(config.wps.pin);
            holder.binding.iwdaServerStop.setOnClickListener(v -> {
                int tempI = 0;
                Long port = null;
                for (Map.Entry<Long, WifiP2pDevice> entry : deviceMap.entrySet()) {
                    if (holder.getBindingAdapterPosition() == tempI) {
                        port = entry.getKey();
                        break;
                    }
                    tempI += 1;
                }
                connectorExampleI.stopServerOnPort(port);
            });
        }
    }

    @Override
    public int getItemCount() {
        return deviceMap.size();
    }

    public void setDeviceMaps(Map<Long, WifiP2pDevice> deviceMap, Map<Long, WifiP2pConfig> deviceConfigMap) {
        this.deviceMap = deviceMap;
        this.deviceConfigMap = deviceConfigMap;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemWifiDirectActiveBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemWifiDirectActiveBinding.bind(itemView);
        }
    }
}
