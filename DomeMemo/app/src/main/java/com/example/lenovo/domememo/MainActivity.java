package com.example.lenovo.domememo;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {
    public ArrayList list=new ArrayList();
    private ListView listview;
    private MyAdapter myAdapter;
    /**列表的数据源*/
    private List<String> listData;
    /**记录选中item的下标*/
    private List<Integer> checkedIndexList;
    /**保存每个item中的checkbox*/
    private List<CheckBox> checkBoxList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    /**初始化列表的数据源*/
    public void initListData() {
        //静态赋值
        listData = new ArrayList<String>();
        for(int i=0;i<2;i++){
            listData.add("item" + i);
        }
    }
    //添加事件
    public void Click(View view){
        EditText text=(EditText)findViewById(R.id.e);
        String s=text.getText().toString();
        list.add(s);
        ListView listView=(ListView)findViewById(R.id.listview);
        ArrayAdapter<String> ad =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(ad);
        ((EditText) findViewById(R.id.e)).setText("");
    }
    /**初始化控件*/
    public void initView(){
        listview = (ListView) findViewById(R.id.listview);
        myAdapter = new MyAdapter(getApplicationContext(), listData);
        listview.setAdapter(myAdapter);
        //监听listview的长按事件
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                //将checkbox设置为可见
                for(int i=0;i<checkBoxList.size();i++){
                    checkBoxList.get(i).setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        checkedIndexList = new ArrayList<Integer>();
        checkBoxList = new ArrayList<CheckBox>();
    }
    /**自定义listview的适配器*/
    class MyAdapter extends BaseAdapter {
        private List<String> listData;
        private LayoutInflater inflater;
        public MyAdapter(Context context, List<String> listData){
            this.listData = listData;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return listData.size();
        }
        @Override
        public Object getItem(int arg0) {
            return listData.get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.textview);
                viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                //将item中的checkbox放到checkBoxList中
                checkBoxList.add(viewHolder.checkbox);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv.setText(listData.get(position));
            viewHolder.checkbox.setOnCheckedChangeListener(new CheckBoxListener(position));
            return convertView;
        }
        class ViewHolder{
            TextView tv;
            CheckBox checkbox;
        }
    }
    /**checkbox的监听器*/
    class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        /**列表item的下标位置*/
        int position;
        public CheckBoxListener(int position){
            this.position = position;
        }
        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
            if(isChecked){
                checkedIndexList.add(position);
            }else{
                checkedIndexList.remove((Integer)position);
            }
        }
    }
    //编辑事件
    public void click_deleteButton(View view){
        //将checkbox设置为可见
        for(int i=0;i<checkBoxList.size();i++){
            checkBoxList.get(i).setVisibility(View.VISIBLE);
        }

    }
//删除事件
    public void  click_cancelButton(View v){
//先将checkedIndexList中的元素从大到小排列,否则可能会出现错位删除或下标溢出的错误
        checkedIndexList = sortCheckedIndexList(checkedIndexList);
        for(int i=0;i<checkedIndexList.size();i++){
            //需要强转为int,才会删除对应下标的数据,否则默认删除与括号中对象相同的数据
            listData.remove((int)checkedIndexList.get(i));
            checkBoxList.remove(checkedIndexList.get(i));
        }
        for(int i=0;i<checkBoxList.size();i++){
            //将已选的设置成未选状态
            checkBoxList.get(i).setChecked(false);
            //将checkbox设置为不可见
            checkBoxList.get(i).setVisibility(View.INVISIBLE);
        }
        //更新数据源
        myAdapter.notifyDataSetChanged();
        //清空checkedIndexList,避免影响下一次删除
        checkedIndexList.clear();
    }

    /**对checkedIndexList中的数据进行从大到小排序*/
    public List<Integer> sortCheckedIndexList(List<Integer> list){
        int[] ass = new int[list.size()];//辅助数组
        for(int i=0;i<list.size();i++){
            ass[i] = list.get(i);
        }
        Arrays.sort(ass);
        list.clear();
        for(int i=ass.length-1;i>=0;i--){
            list.add(ass[i]);
        }
        return list;
    }

}
