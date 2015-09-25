package com.br.fundamentosandroid.controllers.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.br.fundamentosandroid.R;
import com.br.fundamentosandroid.controllers.adapter.ServiceOrderListAdapter;
import com.br.fundamentosandroid.exceptions.NotPossibleDisable;
import com.br.fundamentosandroid.models.entities.ServiceOrder;
import com.br.fundamentosandroid.models.service.ServiceOrderBusinessService;
import com.br.fundamentosandroid.util.AppUtil;
import com.br.fundamentosandroid.util.SharedPreferenceUtil;

import org.apache.http.protocol.HTTP;

import java.util.List;

public class ServiceOrderListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final int REQUEST_CODE_ADD = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    private RecyclerView mServiceOrders;
    private ServiceOrderListAdapter mServiceOrdersAdapter;
    private boolean mArchived;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_list_material);
        //FIXME Modo de fazer
        //SharedPreferences userId = getBaseContext().getSharedPreferences("USER", MODE_PRIVATE);
        //int user_id = userId.getInt("USER_ID", 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        bindElements();
    }

    private void bindElements() {
        mServiceOrders = AppUtil.get(findViewById(R.id.recyclerViewServiceOrders));
        mServiceOrders.setHasFixedSize(true);
        mServiceOrders.setLayoutManager(new LinearLayoutManager(this));

        final FloatingActionButton fabAdd = AppUtil.get(findViewById(R.id.fabAdd));
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent goToAddActivity = new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class);
                startActivityForResult(goToAddActivity, REQUEST_CODE_ADD);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateRecyclerItems();
    }

    private void updateRecyclerItems() {
        final List<ServiceOrder> serviceOrders = ServiceOrderBusinessService.getAll(mArchived);
        if (mServiceOrdersAdapter == null) {
            mServiceOrdersAdapter = new ServiceOrderListAdapter(serviceOrders);
            mServiceOrders.setAdapter(mServiceOrdersAdapter);
        } else {
            mServiceOrdersAdapter.setItens(serviceOrders);
            mServiceOrdersAdapter.notifyDataSetChanged();
        }
        // Force onPrepareOptionsMenu call
        supportInvalidateOptionsMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD) {
                Toast.makeText(this, R.string.msg_add_success, Toast.LENGTH_LONG).show();
                // Force onPrepareOptionsMenu call
                supportInvalidateOptionsMenu();
            } else if (requestCode == REQUEST_CODE_EDIT) {
                Toast.makeText(this, R.string.msg_edit_success, Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final ServiceOrder serviceOrder = mServiceOrdersAdapter.getSelectedItem();
        switch (item.getItemId()) {
            case R.id.action_edit:
                final Intent goToEditActivity = new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class);
                goToEditActivity.putExtra(ServiceOrderActivity.EXTRA_SERVICE_ORDER, serviceOrder);
                goToEditActivity.putExtra(ServiceOrderActivity.EXTRA_START_BENCHMARK, SystemClock.elapsedRealtime());
                super.startActivityForResult(goToEditActivity, REQUEST_CODE_EDIT);
                return true;
            case R.id.action_delete:
                deleteServiceOrder(serviceOrder);
                return true;
            case R.id.action_call:
                // Best Practices: http://stackoverflow.com/questions/4275678/how-to-make-phone-call-using-intent-in-android
                final Intent goToSOPhoneCall = new Intent(Intent.ACTION_CALL /* or Intent.ACTION_DIAL (no manifest permission needed) */);
                goToSOPhoneCall.setData(Uri.parse("tel:" + serviceOrder.getPhone()));
                startActivity(goToSOPhoneCall);
                return true;
            case R.id.action_archived:
                deactivateServiceOrder(serviceOrder);
                return true;
            case R.id.action_un_archiving:
                activateServiceOrder(serviceOrder);
                return true;
            default:
                return false;
        }
    }

    private void deleteServiceOrder(final ServiceOrder serviceOrder) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.lbl_confirm)
                .setMessage(R.string.msg_confirm_delete)
                .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ServiceOrderBusinessService.delete(serviceOrder);
                            Toast.makeText(ServiceOrderListActivity.this, R.string.msg_delete_success, Toast.LENGTH_SHORT).show();
                            updateRecyclerItems();
                            supportInvalidateOptionsMenu();
                        } catch (NotPossibleDisable notPossibleDisable) {
                            Toast.makeText(ServiceOrderListActivity.this, notPossibleDisable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNeutralButton(R.string.lbl_no, null)
                .create().show();
    }

    private void deactivateServiceOrder(final ServiceOrder serviceOrder) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.lbl_confirm)
                .setMessage(R.string.msg_confirm_archived)
                .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ServiceOrderBusinessService.disable(serviceOrder);
                            Toast.makeText(ServiceOrderListActivity.this, R.string.msg_archived_success, Toast.LENGTH_SHORT).show();
                            updateRecyclerItems();
                            supportInvalidateOptionsMenu();
                        } catch (NotPossibleDisable notPossibleDisable) {
                            Toast.makeText(ServiceOrderListActivity.this, notPossibleDisable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNeutralButton(R.string.lbl_no, null)
                .create().show();
    }

    private void activateServiceOrder(final ServiceOrder serviceOrder) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.lbl_confirm)
                .setMessage(R.string.msg_confirm_active)
                .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ServiceOrderBusinessService.active(serviceOrder);
                        Toast.makeText(ServiceOrderListActivity.this, R.string.msg_active_success, Toast.LENGTH_SHORT).show();
                        updateRecyclerItems();
                        supportInvalidateOptionsMenu();
                    }
                })
                .setNeutralButton(R.string.lbl_no, null)
                .create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_service_order_list_actionbar, menu);
        MenuItem archived = menu.findItem(R.id.actionSearchArquivados);
        MenuItem noArchived = menu.findItem(R.id.actionSearchNaoArquivados);

        if (mArchived) {
            archived.setChecked(true);
        } else {
            noArchived.setChecked(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @see <a href="http://developer.android.com/guide/components/intents-filters.html">Forcing an app chooser</a>
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionShare:
                final Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ServiceOrderBusinessService.getAll(true).toString());
                sendIntent.setType(HTTP.PLAIN_TEXT_TYPE);
                final Intent chooser = Intent.createChooser(sendIntent, getString(R.string.lbl_share_option));
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                return true;
            case R.id.actionSearchNaoArquivados:
                mArchived = false;
                updateRecyclerItems();
                return true;
            case R.id.actionSearchArquivados:
                mArchived = true;
                updateRecyclerItems();
                return true;
            case R.id.action_power:
                SharedPreferenceUtil.disconnectUser();
                startActivity(new Intent(ServiceOrderListActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem menuShare = menu.findItem(R.id.actionShare);
        final boolean menuShareVisible = mServiceOrdersAdapter.getItemCount() > 0;
        menuShare.setEnabled(menuShareVisible).setVisible(menuShareVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "SAIR", Toast.LENGTH_SHORT).show();
    }
}
