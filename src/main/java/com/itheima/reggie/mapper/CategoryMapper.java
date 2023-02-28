package com.itheima.reggie.mapper;

import com.itheima.reggie.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CategoryMapper {
    @Select("select * from category order by sort asc")
    List<Category> findAll();

    @Insert("insert into category values(null,#{type},#{name},#{sort},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(Category category);

    @Update("update category set name = #{name},sort = #{sort},update_time = #{updateTime},update_user = #{updateUser} where id = #{id}")
    void update(Category category);

    @Delete("delete from category where id = #{id}")
    void delete(Long id);

    /*
    @Select("<script>select * from category\n" +
            "        <where>\n" +
            "            <if test=\"type != null\">\n" +
            "                type = #{type}\n" +
            "            </if>\n" +
            "        </where></script>")
    */
    List<Category> findByType(Integer type);

    @Select("select * from `category`")
    List<Category> getList();
}
