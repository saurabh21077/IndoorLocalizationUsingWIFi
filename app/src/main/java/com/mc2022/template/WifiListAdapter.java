package com.mc2022.template;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mc2022.template.database.WifiDataDatabase;
import com.mc2022.template.model.WifiData;

import java.util.List;

public class WifiListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<ScanResult> listOfWifi;
    //int wifiRSSI;


    public WifiListAdapter(Context context, List<ScanResult> listOfWifi) {
        this.context = context;
        this.listOfWifi = listOfWifi;
        //  this.wifiRSSI = wifiRSSI;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listOfWifi.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfWifi.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();

        if(view == null){
            view = layoutInflater.inflate(R.layout.wifi_list_item, null);
            viewHolder.wifiItemName = view.findViewById(R.id.textView_wifiName);
            viewHolder.wifiRssi = view.findViewById(R.id.textView_rssiValue);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        WifiDataDatabase wifiDatabase;
        wifiDatabase = WifiDataDatabase.getInstance(context);
        WifiData wifiData = new WifiData(listOfWifi.get(i).SSID, listOfWifi.get(i).level);
        wifiDatabase.wifiDataDAO().insert(wifiData);

        // Get the list of Wi-Fi's
        List<WifiData> wifiEntries = wifiDatabase.wifiDataDAO().getWifiDataInDescOrder();

//        String wifiName = wifiEntries.get(i).getName();
//        String rssi = Integer.toString(wifiEntries.get(i).getRssi());

        String wifiName = listOfWifi.get(i).SSID;
        String rssi = Integer.toString(listOfWifi.get(i).level);

        viewHolder.wifiItemName.setText(wifiName);
        viewHolder.wifiRssi.setText(rssi);

        return view;
    }

    class ViewHolder{
        private TextView wifiItemName;
        private TextView wifiRssi;
    }

}