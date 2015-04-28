package com.brunocesar.trackmypack.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.brunocesar.trackmypack.database.PackageDataSource;
import com.brunocesar.trackmypack.enums.Operation;
import com.brunocesar.trackmypack.models.Package;
import com.brunocesar.trackyourpack.R;

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

        //Show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        operation = (Operation) getIntent().getExtras().get("operation");
        pack = (Package) getIntent().getExtras().getSerializable("data");

        if (pack == null)
            pack = new Package();

        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        codeEditText = (EditText) findViewById(R.id.code_edit_text);

        packageDataSource = new PackageDataSource(this);
        packageDataSource.open();

        if (operation == Operation.Add)
            setTitle("Novo Pacote");
        else {
            setTitle("Editar Pacote");

            nameEditText.setText(pack.getName());
            codeEditText.setText(pack.getCode());
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
        getMenuInflater().inflate(R.menu.menu_package_add, menu);
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
            trace("Erro : " + e.getMessage());
        }
    }

    private void updatePackage() {

        loadFromViewToPackage();

        if (validatePackage()) {
            packageDataSource.update(pack);

            Intent intent = new Intent(this, PackageDetailsActivity.class);
            intent.putExtra("package", pack);
            intent.putExtra("operation", operation);
            setResult(Activity.RESULT_OK, intent);
            this.finish();

            toast("Pacote editado com sucesso!");
        }
    }

    private void addPackage() {

        loadFromViewToPackage();

        if (validatePackage()) {
            packageDataSource.create(pack);

            Intent intent = new Intent(this, PackageListActivity.class);
            intent.putExtra("package", pack);
            intent.putExtra("operation", operation);
            setResult(Activity.RESULT_OK, intent);
            this.finish();

            toast("Pacote salvo com sucesso!");
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
            codeEditText.setError("Campo obrigatório");
        }

        if (pack.getName().isEmpty()) {
            isValid = false;
            nameEditText.setError("Campo obrigatório");
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
