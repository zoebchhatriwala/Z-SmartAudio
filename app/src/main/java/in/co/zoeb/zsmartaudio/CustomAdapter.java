package in.co.zoeb.zsmartaudio;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {

    LayoutInflater mInflater;
    private String[] mNames, mFilelocation;
    private Context mContext;


    public CustomAdapter(Context context, String[] names, String[] filelocation) {
        super(context, R.layout.listviewlayout, names);
        mContext = context;
        mNames = names;
        mFilelocation = filelocation;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listviewlayout, null);

        }

        final viewholder viewholder = new viewholder();
        viewholder.text = (TextView) convertView.findViewById(R.id.text);
        viewholder.subtext = (TextView) convertView.findViewById(R.id.subtext);
        viewholder.icon = (ImageView) convertView.findViewById(R.id.photo);
        viewholder.check = (CheckBox) convertView.findViewById(R.id.checkBox);

        viewholder.text.setTag(position);
        viewholder.text.setText(mNames[position]);
        viewholder.subtext.setText(mFilelocation[position]);
        viewholder.icon.setImageResource(R.drawable.audio);
        viewholder.check.setTag(position);
        viewholder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Audio.arrayPosition.add(Integer.parseInt(buttonView.getTag().toString()));
                } else {
                    for (int i = 0; i < Audio.arrayPosition.size(); i++) {
                        if (Audio.arrayPosition.get(i) == buttonView.getTag()) {
                            Audio.arrayPosition.remove(i);
                        }
                    }
                }
            }
        });
        return convertView;

    }

    public class viewholder {
        TextView text, subtext;
        ImageView icon;
        CheckBox check;
    }

}