package mainactivity.katherineosorio.com.practica6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private static DataBaseManager Manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Manager = new DataBaseManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static DataBaseManager getManager() {
        return Manager;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.fragment:
                Intent i = new Intent(this,Fragment.class);
                startActivity(i);
                return true;
            case R.id.mapas:
                Intent j = new Intent(this,Mapas.class);
                startActivity(j);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
