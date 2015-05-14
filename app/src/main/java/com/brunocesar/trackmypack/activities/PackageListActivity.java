package com.brunocesar.trackmypack.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.brunocesar.trackmypack.R;
import com.brunocesar.trackmypack.database.DatabaseHelper;
import com.brunocesar.trackmypack.database.PackageDataSource;
import com.brunocesar.trackmypack.models.Package;
import com.brunocesar.trackmypack.adapters.PackageAdapter;
import com.brunocesar.trackmypack.enums.Operation;

import java.util.List;

public class PackageListActivity extends ActionBarActivity {

    private PackageDataSource packageDataSource;
    private PackageAdapter packagesAdapter;
    private int selectedItemPosition;
    private List<com.brunocesar.trackmypack.models.Package> packages;
    private TextView packageMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" " + getString(R.string.app_name));
        actionBar.setSubtitle(" " + getString(R.string.list_package_title));

        packageDataSource = new PackageDataSource(this);
        packageDataSource.open();

        ListView packageListView = (ListView) findViewById(R.id.package_list_view);
        packageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Package pack = packagesAdapter.getItem(position);
                selectedItemPosition = position;
                detailPackage(pack);
            }
        });

        packages = packageDataSource.get(null, DatabaseHelper.PACKAGES_ID + " DESC");
        packagesAdapter = new PackageAdapter(this, packages);
        packageListView.setAdapter(packagesAdapter);

        selectedItemPosition = -1;

        packageMessageTextView = (TextView)findViewById(R.id.package_message_text_view);
        checkMessageDisplay();
    }

    private void checkMessageDisplay() {
        if(packages.size() > 0)
            packageMessageTextView.setVisibility(View.GONE);
        else {
            packageMessageTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        packageDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        packageDataSource.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_package_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new_package:
                addPackage();
                break;

            //case R.id.action_refresh:
                //Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                  //      .show();
                //break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            Package pack = (Package) data.getExtras().get("package");
            Operation operation = (Operation) data.getExtras().get("operation");

            if (operation == Operation.Add)
                packagesAdapter.insert(pack, 0);
            else if (operation == Operation.Update) {
                packages.remove(selectedItemPosition);
                packages.add(selectedItemPosition, pack);
            }
            else
                packages.remove(selectedItemPosition);

            packagesAdapter.notifyDataSetChanged();
            checkMessageDisplay();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addPackage() {
        Intent intent = new Intent(this, PackageAddUpdateActivity.class);
        intent.putExtra("operation", Operation.Add);
        startActivityForResult(intent, Operation.Add.ordinal());
    }

    private void detailPackage(Package pack) {
        Intent intent = new Intent(this, PackageDetailsActivity.class);
        intent.putExtra("operation", Operation.Details);
        intent.putExtra("data", pack);
        startActivityForResult(intent, Operation.Details.ordinal());
    }
}
