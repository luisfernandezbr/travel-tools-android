package br.com.mobiplus.ferramentadeviajem.mvp.repository;

/**
 * Created by luisfernandez on 02/11/17.
 */

public interface DataCallback<SUCCESS, ERROR>
{
    void onSuccess(SUCCESS success);
    void onError(ERROR error);
}
