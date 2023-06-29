package cn.hanglok;

import cn.hanglok.config.DataSourceConfig;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className MybatisPlusGenerator
 * @description TODO
 * @date 2023/6/7 14:16
 */
@SpringBootTest
public class MybatisPlusGeneratorTest {

    // MP生成代码指定目录
    private final String outDir = System.getProperty("user.dir") + MessageFormat.format("{0}src{0}main{0}java", File.separator);
    // MP生成xmp文件指定目录
    private final String xmlOutDir = System.getProperty("user.dir") + MessageFormat.format("{0}src{0}main{0}resources{0}mapper", File.separator);

    /**
     * 快速生成
     */
    @Test
    public void fastGeneratorCode() {
        FastAutoGenerator.create(DataSourceConfig.url, DataSourceConfig.username, DataSourceConfig.password)
                .globalConfig(builder -> {
                    builder.author("Allen") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式，不需要开启
//                            .fileOverride() // 覆盖已生成文件，已失效。未在策略配置中找到此配置
                            .outputDir(outDir); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("cn.hanglok") // 设置父包名
                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("institution"); // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateConfig(config -> {
                    config.disable(TemplateType.CONTROLLER);
                })
                .execute();
    }

    /**
     * 交互式生成
     */
    @Test
    public void interactiveGeneratorCode() {
        FastAutoGenerator.create(DataSourceConfig.url, DataSourceConfig.username, DataSourceConfig.password)
                // 全局配置
//                .globalConfig((scanner, builder) -> builder.author(scanner.apply("请输入作者名称？")))
                .globalConfig(builder -> builder.author("Allen").outputDir(outDir))
                // 包配置
//                .packageConfig((scanner, builder) -> builder.parent(scanner.apply("请输入包名？")))
                .packageConfig(builder -> builder.parent("cn.hanglok")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutDir)))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .controllerBuilder().enableRestStyle().enableHyphenStyle()
                        .entityBuilder()
                        // 使用Lombok注解
                        .enableLombok()
                        // 开启文件覆盖模式，谨慎使用
                        // .enableFileOverride()
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT)
                        ).build())
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                 */
                .templateEngine(new FreemarkerTemplateEngine())
                .templateConfig(config -> {
                    // 禁用生成controller
                    config.disable(TemplateType.CONTROLLER);
                    // 禁用生成xml
                    config.disable(TemplateType.XML);
                    // 禁用生成entity
//                    config.disable(TemplateType.ENTITY);
                    // 禁用生成mapper
                    config.disable(TemplateType.MAPPER);
                    // 禁用生成service
                    config.disable(TemplateType.SERVICE);
                    // 禁用生成impl
                    config.disable(TemplateType.SERVICE_IMPL);
                })
                .execute();
    }

    /**
     * 处理 all 情况
     */
    protected List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

}
