package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "购物车管理")
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车：{}",shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车数据")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);

    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        Long id = BaseContext.getCurrentId();
        shoppingCartService.clean(id);
        return Result.success();
    }

    /**
     * 根据菜品或套餐删除购物车数据
     * @param shoppingCartDTO
     */
    @PostMapping("/sub")
    public Result deleteByDishOrSetmeal(@RequestBody ShoppingCartDTO shoppingCartDTO){

//        查询菜品数量
        ShoppingCart shoppingCart = shoppingCartService.selectDishOrSetmeal(shoppingCartDTO);
        int  number = shoppingCart.getNumber();
        Long id = shoppingCart.getId();
        if(number > 1){
            shoppingCart.setNumber(number-1);
            shoppingCartService.update(shoppingCart);
        }
        else {
            shoppingCartService.deleteById(id);
        }
        return Result.success();

    }
}
