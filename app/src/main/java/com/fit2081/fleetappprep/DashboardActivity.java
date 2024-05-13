package com.fit2081.fleetappprep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.fit2081.fleetappprep.provider.Invoice;
import com.fit2081.fleetappprep.provider.InvoiceViewModel;
import com.fit2081.fleetappprep.provider.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class DashboardActivity extends AppCompatActivity {

    ArrayList<Invoice> invoiceList = new ArrayList<>();
    ArrayList<Item> itemList = new ArrayList<>();

    String invoiceId;
    EditText issuerNameInput;
    EditText buyerNameInput;
    String buyerId;
    EditText buyerAddressInput;
    Switch isPaidInput;
    int totalValue;
    String itemId;
    EditText itemNameInput;
    EditText itemCostInput;
    EditText itemQuantityInput;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView.LayoutManager itemLayoutManager;
    RecyclerView.LayoutManager invoiceLayoutManager;
    itemRecyclerViewAdapter itemRecyclerViewAdapter;
    invoiceRecyclerViewAdapter invoiceRecyclerViewAdapter;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    FloatingActionButton fab;
    InvoiceViewModel viewModel;
    TextView totalTv;
    Button wikiButton;
    GestureDetectorCompat gestureDetector;
    View activityFrame;
    Button itemWikiButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_dashboard);

        issuerNameInput = findViewById(R.id.issuer_input);
        buyerNameInput = findViewById(R.id.buyer_input);
        buyerAddressInput = findViewById(R.id.address_input);
        isPaidInput = findViewById(R.id.is_paid_switch);
        itemNameInput = findViewById(R.id.item_name_input);
        itemCostInput = findViewById(R.id.item_cost_input);
        itemQuantityInput = findViewById(R.id.item_quantity_input);
        wikiButton=findViewById(R.id.wiki_button);
        sharedPreferences = getSharedPreferences("INVOICE_DATA",Context.MODE_PRIVATE);
        totalTv=findViewById(R.id.invoice_total_id);
        fab = findViewById(R.id.fab);
        activityFrame = findViewById(R.id.activity_frame);
        gestureDetector = new GestureDetectorCompat(this, new GestureListener());
        itemWikiButton = findViewById(R.id.wiki_button);

        fab.setOnClickListener(new FabListener());

        //toolbar + drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open,R.string.nav_close);
        toggle.syncState();

        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavListener());

        //Broadcast Receiver
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS}, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        registerReceiver(myBroadCastReceiver,new IntentFilter(MySMSReceiver.SMS_FILTER),RECEIVER_EXPORTED);

        //view model
        viewModel=new ViewModelProvider(this).get(InvoiceViewModel.class);

        //Item Recycler View
        RecyclerView itemRecyclerView = findViewById(R.id.item_recyclerView);
        itemLayoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        itemRecyclerView.setLayoutManager(itemLayoutManager);   // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager

        itemRecyclerViewAdapter = new itemRecyclerViewAdapter();
        itemRecyclerViewAdapter.setData(itemList);
        itemRecyclerView.setAdapter(itemRecyclerViewAdapter);

        //Invoice Fragment
//        getSupportFragmentManager().beginTransaction().replace(R.id.invoice_frame_layout, new InvoiceFragment()).commit();

        wikiButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebActivity.class);
            String url = "https://en.wikipedia.org/wiki/" + itemNameInput.getText().toString();
            intent.putExtra("url", url);
            startActivity(intent);
        });

        activityFrame.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent){
                gestureDetector.onTouchEvent(motionEvent);

                return true;
            }
        });

    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
//            saveInvoice();
            Log.d("Gesture", "Double Tap");
            addInvoice();
            return true;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            Log.d("Gesture", "Long Press");
            addItem();
        }

        @Override
        public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            Log.d("Gesture", "Distance X=" + distanceX + " Distance Y=" + distanceY);
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                if (distanceX > 0) {
                    //Decrement
                    itemCostInput.setText(String.valueOf(Math.max(0, Integer.parseInt(itemCostInput.getText().toString()) - Math.abs((int)distanceX))));
                } else {
                    //Increment
                    itemCostInput.setText(String.valueOf(Math.max(0, Integer.parseInt(itemCostInput.getText().toString()) + Math.abs((int)distanceX))));
                }
            } else {
                if (distanceY > 0) {
                    //Decrement
                    itemQuantityInput.setText(String.valueOf((int)Math.max(0, Double.parseDouble(itemQuantityInput.getText().toString()) + Math.abs((int)distanceY))));
                } else {
                    //Increment
                    itemQuantityInput.setText(String.valueOf((int)Math.max(0, Double.parseDouble(itemQuantityInput.getText().toString()) - Math.abs((int)distanceY))));
                }

            }
            return true;
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String msg = intent.getStringExtra(MySMSReceiver.SMS_MSG_KEY);

            StringTokenizer sT = new StringTokenizer(msg, ":;");
            String msgType = sT.nextToken();

            if(msgType.equals("invoice")){
                try{
                    String issuerName = sT.nextToken();
                    String buyerName = sT.nextToken();
                    String buyerAddress = sT.nextToken();
                    String isPaid = sT.nextToken();

                    issuerNameInput.setText(issuerName);
                    buyerNameInput.setText(buyerName);
                    buyerAddressInput.setText(buyerAddress);
                    isPaidInput.setChecked(Boolean.parseBoolean(isPaid));
                } catch (Exception e){
                    Log.d("Message Received", "Invalid Text");
                }
            }
            else if(msgType.equals("item")){
                try{
                    String itemName = sT.nextToken();
                    String itemQuantity = sT.nextToken();
                    String itemCost = sT.nextToken();

                    Integer.parseInt(itemQuantity);
                    Integer.parseInt(itemCost);

                    itemNameInput.setText(itemName);
                    itemQuantityInput.setText(itemQuantity);
                    itemCostInput.setText(itemCost);
                } catch (Exception e){
                    Log.d("Message Received", "Invalid Text");
                }
            }
            else if(msgType.equals("load") && sT.nextToken().equals("invoice")){
                loadInvoice();

            }
            else if(msgType.equals("save") && sT.nextToken().equals("invoice")){
                saveInvoice();
            }
        }
    }

    public void saveInvoice(){
        //Saves the Invoice to the Shared Preferences
        try {
            editor = sharedPreferences.edit();
            //Required
            String issuerName = issuerNameInput.getText().toString();
            String buyerName = buyerNameInput.getText().toString();
            String buyerAddress = buyerAddressInput.getText().toString();
            String itemName = itemNameInput.getText().toString();
            int itemQuantity = Integer.parseInt(itemQuantityInput.getText().toString());
            int itemCost = Integer.parseInt(itemCostInput.getText().toString());
            //Not Required
            String invoiceId = generateInvoiceID();
            String buyerId = generateBuyerID();
            String itemId = generateItemID();
            boolean isPaid = isPaidInput.isChecked();

            if (issuerName.trim().length() == 0 || buyerName.trim().length() == 0 || buyerAddress.trim().length() == 0
                    || itemName.trim().length() == 0 || String.valueOf(itemQuantity).trim().length() == 0 || String.valueOf(itemCost).trim().length() == 0) {
                Toast.makeText(this, "Invalid: Not All Required Inputs Have Been Filled", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("invoiceId", invoiceId);
                editor.putString("buyerId", buyerId);
                editor.putString("itemId", itemId);
                editor.putString("issuerName", issuerName);
                editor.putString("buyerName", buyerName);
                editor.putString("buyerAddress", buyerAddress);
                editor.putString("itemName", itemName);
                editor.putInt("itemQuantity", itemQuantity);
                editor.putInt("itemCost", itemCost);
                editor.putBoolean("isPaid", isPaid);
                editor.apply();

                String invoiceFormat = "Invoice successfully saved: InvoiceID: %s, BuyerID: %s, ItemID: %s";
                String invoiceStr = String.format(invoiceFormat, invoiceId, buyerId, itemId);
                Toast.makeText(this, invoiceStr, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this, "Invalid: Not All Required Inputs Have Been Filled", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadInvoice(){
        //Loads the invoice from Shared Preferences to the invoice dashboard

        String issuerName = sharedPreferences.getString("issuerName", "");
        String buyerName = sharedPreferences.getString("buyerName", "");
        String buyerAddress = sharedPreferences.getString("buyerAddress", "");
        boolean isPaid = sharedPreferences.getBoolean("isPaid", false);
        String itemName = sharedPreferences.getString("itemName", "");
        String itemQuantity = String.valueOf(sharedPreferences.getInt("itemQuantity", 0));
        String itemCost = String.valueOf(sharedPreferences.getInt("itemCost", 0));

        issuerNameInput.setText(issuerName);
        buyerNameInput.setText(buyerName);
        buyerAddressInput.setText(buyerAddress);
        isPaidInput.setChecked(isPaid);
        itemNameInput.setText(itemName);
        itemQuantityInput.setText(itemQuantity);
        itemCostInput.setText(itemCost);

        Toast.makeText(this,"Invoice Loaded Successfully", Toast.LENGTH_SHORT).show();
    }

    private String generateInvoiceID(){
        invoiceId = "I";

        Random random = new Random();

        for(int i=0; i < 2; i++){
            invoiceId += (char)(random.nextInt(26) + 'A');
        }

        invoiceId += "-";

        for(int i=0; i < 4; i++){
            invoiceId += (int) random.nextInt(10);
        }

        return invoiceId;
    }

    private String generateBuyerID(){
        String buyerName = buyerNameInput.getText().toString();

        invoiceId = "B";

        for(int i=0; i < 2; i++){
            invoiceId += Character.toUpperCase(buyerName.charAt(i));
        }

        invoiceId += "-";

        Random random = new Random();

        for(int i=0; i < 3; i++){
            invoiceId += (int) random.nextInt(10);
        }

        return invoiceId;
    }

    private String generateItemID(){
        String itemName = itemNameInput.getText().toString();

        invoiceId = "T";

        for(int i=0; i < 2; i++){
            invoiceId += Character.toUpperCase(itemName.charAt(i));
        }

        invoiceId += "-";

        Random random = new Random();

        for(int i=0; i < 4; i++){
            invoiceId += (int) random.nextInt(10);
        }

        return invoiceId;
    }

    private void addItem(){
        String itemName = itemNameInput.getText().toString();
        int invoiceTotal = Integer.parseInt(totalTv.getText().toString());
        int quantity = Integer.parseInt(itemQuantityInput.getText().toString());
        int itemCost = Integer.parseInt(itemCostInput.getText().toString());
        Item item = new Item(itemName, quantity,itemCost);

        if (itemName.trim().length() == 0 || String.valueOf(quantity).trim().length() == 0 || String.valueOf(itemCost).trim().length() == 0) {
            Toast.makeText(this, "Invalid: Not All Required Inputs Have Been Filled", Toast.LENGTH_SHORT).show();
        } else{
            invoiceTotal += quantity*itemCost;
            totalTv.setText(String.valueOf(invoiceTotal));
            itemList.add(item);
            itemRecyclerViewAdapter.notifyDataSetChanged();
        }

    }

    private void addInvoice(){
        try {
            totalValue = 0;
            String invoiceId = generateInvoiceID();
            String issuerName = issuerNameInput.getText().toString();
            String buyerId = generateBuyerID();
            String buyerName = buyerNameInput.getText().toString();
            String buyerAddress = buyerAddressInput.getText().toString();
            Boolean isPaid = isPaidInput.isChecked();

            if (buyerName.trim().length() == 0 || issuerName.trim().length() == 0 || buyerAddress.trim().length() == 0) {
                Toast.makeText(this, "Issuer name, Buyer name and Buyer Address need to be specified", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < itemList.size(); i++) {
                    totalValue += itemList.get(i).getCost() * itemList.get(i).getQuantity();
                }

                Invoice invoice = new Invoice(invoiceId, issuerName, buyerId, buyerName, buyerAddress, isPaid, itemList, totalValue);

                totalTv.setText("0");
                itemList.clear();
                itemRecyclerViewAdapter.notifyDataSetChanged();

                invoiceList.add(invoice);
                viewModel.insert(invoice);
            }
        } catch (Exception e){
            Toast.makeText(this, "An error occurred, Issuer Name, Buyer Name and Buyer Address need to be filled",Toast.LENGTH_SHORT).show();
        }
    }

    class NavListener implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();
            if(id==R.id.nav_menu_add_item){
                addItem();
            }
            else if(id==R.id.nav_menu_add_invoice){
                addInvoice();
            }
            else if(id==R.id.nav_menu_clear_fields){
                clearFields();
            }
            else if(id==R.id.nav_menu_maps){
                goToMaps();
            }
            else if(id==R.id.nav_menu_list_invoices){
                viewInvoices();
            }
            else if(id==R.id.nav_menu_exit){
                finish();
                System.exit(0);
            }
            return true;
        }
    }

    class FabListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            addInvoice();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.option_menu_add_item){
            addItem();
        }
        else if(id==R.id.option_menu_clear_fields){
            clearFields();
        }
        return true;
    }

    private void clearFields(){
        issuerNameInput.setText("");
        buyerNameInput.setText("");
        buyerAddressInput.setText("");
        itemNameInput.setText("");
        itemQuantityInput.setText("");
        itemCostInput.setText("");
        isPaidInput.setChecked(false);

    }

    public void viewInvoices(){
        Intent intent = new Intent(this, ViewInvoicesActivity.class);
        startActivity(intent);
    }

    private void goToMaps(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}