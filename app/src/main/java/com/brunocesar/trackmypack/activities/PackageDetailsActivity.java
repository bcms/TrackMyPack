package com.brunocesar.trackmypack.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brunocesar.trackmypack.R;
import com.brunocesar.trackmypack.adapters.HistoryAdapter;
import com.brunocesar.trackmypack.database.DatabaseHelper;
import com.brunocesar.trackmypack.database.HistoryDataSource;
import com.brunocesar.trackmypack.database.PackageDataSource;
import com.brunocesar.trackmypack.enums.Operation;
import com.brunocesar.trackmypack.http.HistoryHttp;
import com.brunocesar.trackmypack.http.TaskListener;
import com.brunocesar.trackmypack.models.History;
import com.brunocesar.trackmypack.models.Package;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackageDetailsActivity extends ActionBarActivity {

    private com.brunocesar.trackmypack.models.Package pack;
    private List<History> histories;

    private boolean changedPack;

    private PackageDataSource packageDataSource;
    private HistoryDataSource historyDataSource;

    private ActionBar actionBar;
    private TextView nameTextView;
    private TextView codeTextView;
    private HistoryAdapter historyAdapter;

    private TextView historyMessageTextView;

    private HistoryHttp historyHttp;

    private TaskListener<List<History>> historyTaskListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        final PackageDetailsActivity context = this;

        pack = (Package) getIntent().getExtras().getSerializable("data");

        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setSubtitle(pack.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameTextView = (TextView) findViewById(R.id.name_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);
        historyMessageTextView = (TextView) findViewById(R.id.history_message_text_view);
        ListView historyListView = (ListView) findViewById(R.id.history_list_view);

        changedPack = false;

        packageDataSource = new PackageDataSource(this);
        packageDataSource.open();

        historyDataSource = new HistoryDataSource(this);
        historyDataSource.open();

        histories = historyDataSource.get(DatabaseHelper.HISTORY_ID_PACKAGE + " = " + pack.getId(), DatabaseHelper.HISTORY_ID + " DESC");

        historyAdapter = new HistoryAdapter(this, histories);
        historyListView.setAdapter(historyAdapter);

        checkMessageDisplay(false);

        historyHttp = new HistoryHttp();
        historyTaskListener = new TaskListener<List<History>>() {
            @Override
            public void onFinished(List<History> newHistories) {

                boolean notifyChanges = false;

                if (newHistories == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.verify_connection_message);
                    builder.setPositiveButton(R.string.ok_message, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    if (newHistories.size() == 0 && histories.size() > 0) {

                        List<History> historiesToDelete = historyDataSource.get(DatabaseHelper.HISTORY_ID_PACKAGE + " = " + pack.getId(), null);

                        for (History history : historiesToDelete) {
                            historyDataSource.delete(history);
                        }

                        notifyChanges = true;
                        histories.clear();

                    } else {

                        List<History> historiesToAdd = new ArrayList<>();

                        for (History history : newHistories) {

                            if (historyDataSource.get(DatabaseHelper.HISTORY_DATE + " = '" + history.getDate() + "'", null).size() == 0) {

                                history.setIdPackage(pack.getId());
                                history = historyDataSource.create(history);
                                historiesToAdd.add(history);

                                notifyChanges = true;
                            }
                        }

                        if (historiesToAdd.size() > 0) {
                            Collections.reverse(historiesToAdd);
                            histories.addAll(historiesToAdd);
                        }
                    }
                }
                checkMessageDisplay(true);

                if (notifyChanges)
                    historyAdapter.notifyDataSetChanged();
            }
        };

        loadViewData();
    }

    @Override
    protected void onResume() {
        packageDataSource.open();
        historyDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        packageDataSource.close();
        historyDataSource.close();
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
            changedPack = true;

            loadViewData();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadViewData() {
        nameTextView.setText(pack.getName());
        codeTextView.setText(pack.getCode());

        actionBar.setSubtitle(pack.getName());

        historyHttp.setTrackCode(pack.getCode());

        try {
            historyHttp.getAllAsync(historyTaskListener);
        } catch (Exception e) {
            toast(e.getMessage());
        }
    }

    private void checkMessageDisplay(boolean afterCheck) {
        if (histories.size() > 0)
            historyMessageTextView.setVisibility(View.GONE);
        else {
            historyMessageTextView.setVisibility(View.VISIBLE);

            if (afterCheck)
                historyMessageTextView.setText(R.string.history_no_history_message);
            else
                historyMessageTextView.setText(R.string.history_loading_message);
        }
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
        builder.setMessage(R.string.delete_package_confirmation_message);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes_message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                packageDataSource.delete(pack);

                Intent intent = new Intent(context, PackageListActivity.class);
                intent.putExtra("package", pack);
                intent.putExtra("operation", Operation.Delete);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        builder.setNegativeButton(R.string.no_message, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
