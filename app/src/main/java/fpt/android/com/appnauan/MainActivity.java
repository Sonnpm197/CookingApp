package fpt.android.com.appnauan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerAdapter recyclerAdapter;
    private SearchView editsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isConnectedWifi() && !isConnectedMobile()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Ứng dụng này yêu cầu có kết nối Internet. Bạn hãy bật wifi hoặc 3G/4G nhé")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // ACTION_MAIN with category CATEGORY_HOME -- Launch the home screen.
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            // CATEGORY_HOME is the first activity that is displayed when the device boots.
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public boolean isConnectedWifi(){
        NetworkInfo info = getNetworkInfo(this);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public boolean isConnectedMobile(){
        NetworkInfo info = getNetworkInfo(this);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    //go top top
    public void setFloatingActionButton(final View view) {
        LinearLayoutManager layoutManager2 = (LinearLayoutManager) layoutManager;
        layoutManager2.scrollToPositionWithOffset(0, 0);
    }

    //set event of item menu(button AI)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ai:
                Intent intent = new Intent(this, ChatBotActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    //get menu and set event of search view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do whatever you need. This will be fired only when submitting.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);//call function to set list when text change
                return false;
            }
        });
        return true;
    }
}
