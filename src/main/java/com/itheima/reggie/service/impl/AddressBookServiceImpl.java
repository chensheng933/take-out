package com.itheima.reggie.service.impl;

import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.mapper.AddressBookMapper;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService{

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public void save(AddressBook addressBook) {
        addressBookMapper.save(addressBook);
    }

    @Override
    public List<AddressBook> queryAddressList(Long userId) {
       List<AddressBook> addressBookList =  addressBookMapper.queryAddressList(userId);
        return addressBookList;
    }

    //更新默认地址
    @Override
    @Transactional
    public void updateDefaultAddress(AddressBook addressBook) {
        //1. 把该用户地址全部修改为非默认
        addressBookMapper.removeDefaultAddress(addressBook.getUserId());
        //2 重新设置当前地址为默认地址
        addressBookMapper.updateDefaultAddress(addressBook);

    }

    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    @Override
    public AddressBook getDefaultAddress(Long currentId) {
        return addressBookMapper.getDefaultAddress(currentId);
    }
}