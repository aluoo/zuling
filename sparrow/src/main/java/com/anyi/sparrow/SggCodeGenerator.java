package com.anyi.sparrow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;

public class SggCodeGenerator {

    public static void main(String[] args) {
        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig.Builder("jdbc:mysql://47.98.124.98:3306/mobile?serverTimezone=GMT%2B8", "root", "af4fc87beb5d")
                .build()
                ;
        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator(dsc);
        // 2、全局配置
        String projectPath = System.getProperty("user.dir");
        GlobalConfig gc = new GlobalConfig.Builder().
                outputDir(projectPath + "/common/src/main/java")
                .author("chenjian")
                .disableOpenDir()
                .enableSwagger()
                .build();
        mpg.global(gc);

        // 4、包配置
        PackageConfig pc = new PackageConfig.Builder()
                .parent("com.anyi.common.insurance")
                .entity("domain")
                .service("service")
                .mapper("mapper")
                .build();
        mpg.packageInfo(pc);


        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig.Builder()
                .addInclude("di_company_account_log,di_company_product_base" +
                        ",di_company_recharge_log,di_insurance,di_insurance_fix_order" +
                        ",di_insurance_fix_order_option,di_insurance_option,di_insurance_user_account,di_option,di_package,di_product_base_config,di_product_insurance,di_type")
                .entityBuilder()
                .enableFileOverride()
                .idType(IdType.ASSIGN_ID)
                .enableLombok()
                .build();
        mpg.strategy(strategy);

        // 6、执行
        mpg.execute();
    }
}