package com.br.fundamentosandroid.models.service;

import com.br.fundamentosandroid.R;
import com.br.fundamentosandroid.exceptions.NotPossibleDisable;
import com.br.fundamentosandroid.models.entities.ServiceOrder;
import com.br.fundamentosandroid.models.persistence.ServiceOrdersRepository;
import com.br.fundamentosandroid.util.AppUtil;

import java.util.List;

/**
 * Created by c1283796 on 17/09/2015.
 */
public class ServiceOrderBusinessService {

    private ServiceOrderBusinessService() {
        super();
    }

    public static void saveOrUpdate(ServiceOrder serviceOrder) {
        ServiceOrdersRepository.saveOrUpdate(serviceOrder);
    }

    public static List<ServiceOrder> getAll(boolean archived) {
        return ServiceOrdersRepository.getAll(archived);
    }

    public static void delete(ServiceOrder serviceOrder) throws NotPossibleDisable {
        if(serviceOrder.isPaid()){
            ServiceOrdersRepository.delete(serviceOrder.getId());
        }else{
            throw new NotPossibleDisable(AppUtil.CONTEXT_APPLICATION.getResources().getString(R.string.msg_no_possible_archived));
        }
    }


    public static void active(ServiceOrder serviceOrder) {
        ServiceOrdersRepository.isActive(serviceOrder);
    }

    public static void disable(ServiceOrder serviceOrder) throws NotPossibleDisable {
        if(serviceOrder.isPaid()){
            ServiceOrdersRepository.disable(serviceOrder);
        }else{
            throw new NotPossibleDisable(AppUtil.CONTEXT_APPLICATION.getResources().getString(R.string.msg_no_possible_archived));
        }
    }

}
