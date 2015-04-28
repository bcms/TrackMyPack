package com.brunocesar.trackmypack.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.brunocesar.trackmypack.database.PackageDataSource;
import com.brunocesar.trackmypack.enums.Operation;
import com.brunocesar.trackmypack.models.Package;
import com.brunocesar.trackyourpack.R;

public class PackageDetailsActivity extends ActionBarActivity {

    private com.brunocesar.trackmypack.models.Package pack;
    private TextView nameTextView;
    private TextView codeTextView;
    private boolean changedPack;
    private PackageDataSource packageDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);

        //Show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pack = (Package) getIntent().getExtras().getSerializable("data");

        nameTextView = (TextView) findViewById(R.id.name_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);

        refreshViewData();

        changedPack = false;

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
        getMenuInflater().inflate(R.menu.menu_package_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                if (changedPack) {
                    Intent intent = new Intent(this, PackageListActivity.class);
                    intent.putExtra("package", pack);
                    intent.putExtra("operation", Operation.Update);
                    setResult(Activity.RESULT_OK, intent);
                    this.finish();
                } else
                    onBackPressed();

                break;

            case R.id.action_update_package:
                updatePackage();
                break;

            case R.id.action_delete_package:
                deletePackage();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            pack = (Package) data.getExtras().get("package");
            refreshViewData();

            changedPack = true;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshViewData() {
        nameTextView.setText(pack.getName());
        codeTextView.setText(pack.getCode());

        setTitle(pack.getName());
    }

    private void updatePackage() {
        Intent intent = new Intent(this, PackageAddUpdateActivity.class);
        intent.putExtra("operation", Operation.Update);
        intent.putExtra("data", pack);
        startActivityForResult(intent, Operation.Update.ordinal());
    }

    private void deletePackage() {

        final PackageDetailsActivity context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tem certeza que deseja excluir este pacote?");
        builder.setCancelable(true);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                
                packageDataSource.delete(pack);
                
                Intent intent = new Intent(context, PackageListActivity.class);
                intent.putExtra("package", pack);
                intent.putExtra("operation", Operation.Delete);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        builder.setNegativeButton("Não", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
