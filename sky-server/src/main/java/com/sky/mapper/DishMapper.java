package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annocation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    /**
     * 插入菜品数据
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品信息删除
     * @param ids
     */
//    void deleteWithIds(Long[] ids);


    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据ID删除菜品信息
     * @param dishId
     */
    @Delete("delete from dish where id = #{dishId}")
    void deleteById(Long dishId);


    /**
     * 菜品信息更新
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);


    /**
     * 菜品信息查询
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

}
