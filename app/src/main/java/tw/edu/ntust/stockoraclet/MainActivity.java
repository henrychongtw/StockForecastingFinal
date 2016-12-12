package tw.edu.ntust.stockoraclet;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import tw.edu.ntust.stockoraclet.loginpackage.activity.LogoutActivity;
import tw.edu.ntust.stockoraclet.loginpackage.helper.SQLiteHandler;
import tw.edu.ntust.stockoraclet.loginpackage.helper.SessionManager;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
//    private String email;
    FragmentManager fragmentManager = getFragmentManager();
    private int id;
    private SQLiteHandler db;
    private SessionManager session;
    private String email ;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        email = user.get("email");
        name = user.get("name");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);
        TextView headerName = (TextView) headerView.findViewById(R.id.nav_name);
        TextView headerEmail = (TextView) headerView.findViewById(R.id.nav_email);
        imageView.setImageResource(R.drawable.logo);
        headerEmail.setText(email);
        headerName.setText(name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dataChange:
                        if(id == R.id.nav_home)
                            fragmentManager.beginTransaction().replace(R.id.content_frame, new home()).commit();
                        else if(id == R.id.nav_hotOraclet)
                            fragmentManager.beginTransaction().replace(R.id.content_frame, new HotOraclet()).commit();
                        else if(id == R.id.nav_subscription)
                            fragmentManager.beginTransaction().replace(R.id.content_frame, new Subscription()).commit();
                        break;
                }
                return true;
            }
        });


        fragmentManager.beginTransaction().replace(R.id.content_frame, new Subscription()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new home()).commit();
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new about()).commit();
        } else if (id == R.id.nav_hotOraclet){
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HotOraclet()).commit();
        } else if (id == R.id.nav_logout){
            fragmentManager.beginTransaction().replace(R.id.content_frame, new LogoutActivity()).commit();
        } else if (id == R.id.nav_subscription){
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Subscription()).commit();
            //fragmentManager.beginTransaction().replace(R.id.content_frame, new home()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

}
