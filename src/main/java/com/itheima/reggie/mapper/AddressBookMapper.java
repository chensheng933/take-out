package com.itheima.reggie.mapper;

import com.itheima.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    //添加地址
    @Insert("insert into address_book(user_id,consignee,phone,sex,detail,label,create_time,update_time," +
            "create_user,update_user) " +
            "values(#{userId},#{consignee},#{phone},#{sex},#{detail},#{label},#{createTime},#{updateTime},#{createUser}," +
            "#{updateUser})")
    void save(AddressBook addressBook);

    //查询地址列表
    @Select("select * from address_book where user_id=#{userId} order by update_time desc")
    List<AddressBook> queryAddressList(Long userId);

    @Update("update address_book set is_default=0 where user_id=#{userId}")
    void removeDefaultAddress(Long userId);

    @Update("update address_book set is_default=1 where id=#{id}")
    void updateDefaultAddress(AddressBook addressBook);

    @Select("select * from address_book where id=#{id}")
    AddressBook getById(Long id);

    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook getDefaultAddress(Long userId);
}