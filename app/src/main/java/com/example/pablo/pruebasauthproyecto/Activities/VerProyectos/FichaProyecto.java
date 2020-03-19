package com.example.pablo.pruebasauthproyecto.Activities.VerProyectos;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.pablo.pruebasauthproyecto.Activities.Navegador;
import com.example.pablo.pruebasauthproyecto.Modelo.Proyecto;
import com.example.pablo.pruebasauthproyecto.Modelo.Usuario;
import com.example.pablo.pruebasauthproyecto.R;

public class FichaProyecto extends AppCompatActivity {

    Proyecto proyecto;
    Usuario usuario;
    String claveProyecto;
    private static final String PROYECTO = "proyecto";
    private static final String USUARIO = "usuario";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_proyecto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Intent intent = getIntent();
        proyecto = (Proyecto) intent.getExtras().getSerializable(Navegador.PROYECTO);
        usuario = (Usuario) intent.getExtras().getSerializable(Navegador.USUARIO);

        Log.d("DEPURACIÓN", proyecto.toString());
        Log.d("DEPURACIÓN", "FichaProyecto -> " + claveProyecto);
        Log.d("DEPURACIÓN", proyecto.getRecursos().toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_ficha_proyecto, menu);
        return true;
    }

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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Bundle bundle = new Bundle();
            bundle.putSerializable(PROYECTO, proyecto);
            bundle.putSerializable(USUARIO, usuario);

            switch (position) {
                case 0:
                    InfoProyecto infoProyecto = new InfoProyecto();
                    infoProyecto.setArguments(bundle);
                    return infoProyecto;
                case 1:
                    AudioProyectos audioProyectos = new AudioProyectos();
                    audioProyectos.setArguments(bundle);
                    return audioProyectos;
                case 2:
                    VideoProyectos videoProyectos = new VideoProyectos();
                    videoProyectos.setArguments(bundle);
                    return videoProyectos;
                case 3:
                    FotosProyecto fotosProyecto = new FotosProyecto();
                    fotosProyecto.setArguments(bundle);
                    return fotosProyecto;
                default:
                    InfoProyecto infoProyecto2 = new InfoProyecto();
                    infoProyecto2.setArguments(bundle);
                    return infoProyecto2;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }
}
