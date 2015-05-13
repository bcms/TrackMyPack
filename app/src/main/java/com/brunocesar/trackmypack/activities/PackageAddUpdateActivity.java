package com.brunocesar.trackmypack.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.brunocesar.trackmypack.R;
import com.brunocesar.trackmypack.database.PackageDataSource;
import com.brunocesar.trackmypack.enums.Operation;
import com.brunocesar.trackmypack.models.Package;

public class PackageAddUpdateActivity extends ActionBarActivity {

    private EditText nameEditText;
    private EditText codeEditText;
    private PackageDataSource packageDataSource;
    private Operation operation;
    private com.brunocesar.trackmypack.models.Package pack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_add_update);

        operation = (Operation) getIntent().getExtras().get("operation");
        pack = (Package) getIntent().getExtras().getSerializable("data");

        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        codeEditText = (EditText) findViewById(R.id.code_edit_text);

        if (pack == null)
            pack = new Package();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.app_name);

        if (operation == Operation.Add)
            actionBar.setSubtitle(R.string.add_package_title);
        else {
            actionBar.setSubtitle(R.string.update_package_title);
            nameEditText.setText(pack.getName());
            codeEditText.setText(pack.getCode());
        }

        packageDataSource = new PackageDataSource(this);
        packageDataSource.open();
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
        getMenuInflater().inflate(R.menu.menu_package_add_udpate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.save_button:
                    if (operation == Operation.Add)
                        addPackage();
                    else
                        updatePackage();
            }

        } catch (Exception e) {
            trace(getString(R.string.error_message) + e.getMessage());
        }
    }

    private void updatePackage() {

        loadFromViewToPackage();

        if (validatePackage()) {
            pack = packageDataSource.update(pack);

            Intent intent = new Intent(this, PackageDetailsActivity.class);
            intent.putExtra("package", pack);
            intent.putExtra("operation", operation);
            setResult(Activity.RESULT_OK, intent);
            this.finish();

            toast(getString(R.string.package_updated_successfully_message));
        }
    }

    private void addPackage() {

        loadFromViewToPackage();

        if (validatePackage()) {
            pack = packageDataSource.create(pack);

            Intent intent = new Intent(this, PackageListActivity.class);
            intent.putExtra("package", pack);
            intent.putExtra("operation", operation);
            setResult(Activity.RESULT_OK, intent);
            this.finish();

            toast(getString(R.string.package_added_successfully_message));
        }
    }

    private void loadFromViewToPackage() {
        pack.setCode(codeEditText.getText().toString());
        pack.setName(nameEditText.getText().toString());
    }

    private boolean validatePackage() {
        boolean isValid = true;

        if (pack.getCode().isEmpty()) {
            isValid = false;
            codeEditText.setError(getString(R.string.required_field_message));
        }

        if (pack.getName().isEmpty()) {
            isValid = false;
            nameEditText.setError(getString(R.string.required_field_message));
        }
        return isValid;
    }


    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void trace(String msg) {
        toast(msg);
    }
}
