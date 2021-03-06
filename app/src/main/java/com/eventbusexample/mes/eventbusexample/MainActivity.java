package com.eventbusexample.mes.eventbusexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//Source: http://www.andreas-schrade.de/2015/11/28/android-how-to-use-the-greenrobot-eventbus/

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    @Bind(R.id.editTextUserInput)
    EditText editTextUser;

    @NonNull @Bind(R.id.textToBeChanged)
    TextView textToBeChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "This is a simple Event Bus implementation.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // Drawer layout
        DrawerLayout          drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        EventBus.getDefault().register(this); // Registration
    }

    @Override
    public void onPause()
    {
        EventBus.getDefault().unregister(this); // Unregistration
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage)
        {
            EventBus.getDefault().post(new MessageEvent("Hello EventBus!")); // The sender
        }
        else if (id == R.id.nav_share)
        {
            sendEmail();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // This method will be called when a MessageEvent is posted
    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event)
    {
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.changeTextBt)
    void changeText()
    {
        textToBeChanged.setText(editTextUser.getText());
    }

    private void sendEmail()
    {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"meskarkala@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "About Event Bus Example");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Add Message here");

        try
        {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


}
