package kg.buhara.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import kg.buhara.R;

public class Main22Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        data();
        firstTest();
    }

    private List<List<String>> getOrders() {

        String[] first = new String[]{"David", "3", "Ceviche"};
        String[] second = new String[]{"Corina", "10", "Beef Burrito"};
        String[] third = new String[]{"David", "3", "Fried Chicken"};
        String[] fourth = new String[]{"Carla", "5", "Water"};
        String[] fifth = new String[]{"Carla", "5", "Ceviche"};
        String[] sixth = new String[]{"Rous", "3", "Ceviche"};

        List<List<String>> orders = new ArrayList<>();
        orders.add(Arrays.asList(first));
        orders.add(Arrays.asList(second));
        orders.add(Arrays.asList(third));
        orders.add(Arrays.asList(fourth));
        orders.add(Arrays.asList(fifth));
        orders.add(Arrays.asList(sixth));

        return orders;
    }

    private void data() {
        List<String> firstRow = new ArrayList<>();
        List<List<String>> result = new ArrayList<>();

        firstRow.add("Table");
        firstRow.addAll(foods());
        result.add(firstRow);

        for (List<String> order : getOrders()) {
            String tableNumber = order.get(1);
            String item = order.get(2);

            List<String> rowValueToBeAddedToResult=new ArrayList<>();
            rowValueToBeAddedToResult.add(String.valueOf(tableNumber));

//            for (String str : foods()) {
//                if(item.contains(str))
//                    rowValueToBeAddedToResult.add(String.valueOf(item.get(str)));
//                else
//                    rowValueToBeAddedToResult.add("0");
//            }
        }
    }

    private List<String> foods() {
        List<String> foods = new ArrayList<>();
        for (List<String> food : getOrders()) {
            if (!food.containsAll(foods)) {
                foods.add(food.get(2));
            }
        }
        java.util.Collections.sort(foods, Collator.getInstance());
        return foods;
    }


///////

    DisplayTableOfFoodsOrdersInARestaurant tableFood;

    public void init(){
        tableFood= new DisplayTableOfFoodsOrdersInARestaurant();
    }

    public void firstTest(){
        tableFood= new DisplayTableOfFoodsOrdersInARestaurant();

        String[] first=new String[]{"David","3","Ceviche"};
        String[] second=new String[]{"Corina","10","Beef Burrito"};
        String[] third=new String[]{"David","3","Fried Chicken"};
        String[] fourth=new String[]{"Carla","5","Water"};
        String[] fifth=new String[]{"Carla","5","Ceviche"};
        String[] sixth=new String[]{"Rous","3","Ceviche"};
        List<List<String>> orders=new ArrayList<>();
        orders.add(Arrays.asList(first));
        orders.add(Arrays.asList(second));
        orders.add(Arrays.asList(third));
        orders.add(Arrays.asList(fourth));
        orders.add(Arrays.asList(fifth));
        orders.add(Arrays.asList(sixth));
        tableFood.displayTable(orders);
    }

    public List<List<String>> displayTable(List<List<String>> orders) {
        Set<String> uniqueItem=new TreeSet<>();
        TreeMap<Integer,HashMap<String,Integer>> tableToFoodCounter=new TreeMap<>();
        for(List<String> order:orders){
            //String name=order.get(0);
            String table=order.get(1);
            String item=order.get(2);
            uniqueItem.add(item);
            if(tableToFoodCounter.containsKey(Integer.valueOf(table))){
                addItem(tableToFoodCounter.get(Integer.valueOf(table)),item);
            }else{
                tableToFoodCounter.put(Integer.valueOf(table),new HashMap<>());
                addItem(tableToFoodCounter.get(Integer.valueOf(table)),item);
            }
        }
        System.out.println(tableToFoodCounter);

        List<List<String>> result=new ArrayList<>();
        List<String> firstRow=new ArrayList<>();
        firstRow.add("Table");
        firstRow.addAll(uniqueItem);
        result.add(firstRow);

        for(Map.Entry<Integer,HashMap<String,Integer>> e:tableToFoodCounter.entrySet()){
            Map<String,Integer> rowData=e.getValue();
            int tableNumber=e.getKey();

            List<String> rowValueToBeAddedToResult=new ArrayList<>();
            rowValueToBeAddedToResult.add(String.valueOf(tableNumber));
            for(String str:uniqueItem){
                if(rowData.containsKey(str))
                    rowValueToBeAddedToResult.add(String.valueOf(rowData.get(str)));
                else
                    rowValueToBeAddedToResult.add("0");
            }
            result.add(rowValueToBeAddedToResult);
        }

        return result;
    }

    private void addItem(HashMap<String, Integer> itemCounter, String item) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            itemCounter.compute(item,(k,v)->v==null?1:v+1);
        }
    }

}
