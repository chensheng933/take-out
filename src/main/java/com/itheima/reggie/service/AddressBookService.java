package com.itheima.reggie.service;

import com.itheima.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService  {

    //保存地址
    void save(AddressBook addressBook);

    //查询地址对象
    List<AddressBook> queryAddressList(Long currentId);

    //更新默认地址
    void updateDefaultAddress(AddressBook addressBook);

    //根据id查询
    AddressBook getById(Long id);

    //得到默认地址
    AddressBook getDefaultAddress(Long currentId);
}