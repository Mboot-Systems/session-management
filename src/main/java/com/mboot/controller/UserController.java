package com.mboot.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.plugins.Page;
import com.mboot.annotation.SystemLog;
import com.mboot.entity.User;
import com.mboot.enums.LogType;
import com.mboot.excel.UserExcel;
import com.mboot.exception.GlobalException;
import com.mboot.result.CodeMsg;
import com.mboot.result.Result;
import com.mboot.service.UserService;
import com.mboot.utils.ExcelUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${user.import.max}")
    private Integer MAX_USER_IMPORT;


    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @RequestMapping("/current")
    @ResponseBody
    public Result<User> current(User user) {
        return Result.success(user);
    }

    @GetMapping
    @SystemLog(description = "Query user list", type = LogType.OPERATION)
    public Result<Page<User>> users(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size, Model model) {
        Page pageable = new Page(page, size);
        Page<User> users = userService.findAll(pageable);
        return Result.success(users);
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody User user) {
        userService.insert(user);
        return Result.success(true);
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody User user) {
        userService.update(user);
        return Result.success(true);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return Result.success(true);
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        return Result.success(user);
    }

    @GetMapping("/excel/template")
    public void downloadTemplate(HttpServletResponse response) {
        String fileName = "Import User Template";
        String sheetName = fileName;

        List<UserExcel> userList = new ArrayList<>();

        userList.add(new UserExcel("mboot", "Words", "123456", "support@mboot.com", "http://xxx.com/xx.jpg", "0", "2020-06-26 12:13:14"));
        userList.add(new UserExcel("mboot2", "Words", "123456", "freelancer@mboot.com", "http://xxx.com/xx.jpg", "0", "2020-06-26 13:14:00"));
        try {
            ExcelUtil.writeExcel(response, userList, fileName, sheetName, UserExcel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/excel/export")
    public void exportData(HttpServletResponse response) {
        String fileName = "user list";
        String sheetName = fileName;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<User> userList = userService.findAll();
        List<UserExcel> userExcelList = new ArrayList<>();
        for (User user : userList) {
            UserExcel userExcel = UserExcel.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .avatar(user.getAvatar())
                    .status(String.valueOf(user.getStatus()))
                    .createdTime(dateFormat.format(user.getCreatedTime())).build();

            userExcelList.add(userExcel);

        }

        try {
            ExcelUtil.writeExcel(response, userExcelList, fileName, sheetName, UserExcel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/excel/import")
    public void importData(MultipartFile file) throws ParseException {
        List<UserExcel> userExcelList = null;
        try {
            userExcelList = EasyExcel.read(new BufferedInputStream(file.getInputStream())).head(UserExcel.class).sheet().doReadSync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userExcelList.size() > MAX_USER_IMPORT) {
            throw new GlobalException(CodeMsg.OVER_MAX_USER_IMPORT_LIMIT.fillArgs(MAX_USER_IMPORT));
        }

        checkImportData(userExcelList);

        List<User> userList = userExcelList2UserList(userExcelList);

        userService.batchInsertOrUpdate(userList);

    }

    @PostMapping("/excel/import/moreSheet")
    public void importDataByMoreSheet(MultipartFile file) throws ParseException, IOException {
        List<UserExcel> userExcelList = new ArrayList<>();

        try {
            ExcelReader excelReader = EasyExcel.read(new BufferedInputStream(file.getInputStream())).head(UserExcel.class).build();
            List<ReadSheet> sheetList = excelReader.excelExecutor().sheetList();
            List<UserExcel> childUserExcelList = new ArrayList<>();
            for (ReadSheet sheet : sheetList) {
                childUserExcelList = EasyExcel.read(new BufferedInputStream(file.getInputStream())).head(UserExcel.class).sheet(sheet.getSheetNo()).doReadSync();
            }
            userExcelList.addAll(childUserExcelList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userExcelList.size() > MAX_USER_IMPORT) {
            throw new GlobalException(CodeMsg.OVER_MAX_USER_IMPORT_LIMIT.fillArgs(MAX_USER_IMPORT));
        }

        checkImportData(userExcelList);

        List<User> userList = userExcelList2UserList(userExcelList);

        userService.batchInsertOrUpdate(userList);

    }

    private void checkImportData(List<UserExcel> userExcelList) {
        int rowNo = 2;
        for (UserExcel userExcel : userExcelList) {
            String username = userExcel.getUsername();
            if (StringUtils.isEmpty(username)) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "username"));
            }
            if (username.length() > 20 || username.length() < 4) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "username"));
            }

            String password = userExcel.getPassword();
            if (StringUtils.isEmpty(password)) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "password"));
            }
            if (password.length() > 100 || password.length() < 6) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "password"));
            }

            String nickname = userExcel.getNickname();
            if (StringUtils.isEmpty(nickname)) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "nick name"));
            }
            if (nickname.length() > 20 || nickname.length() < 2) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "昵称"));
            }

            String email = userExcel.getEmail();
            if (StringUtils.isEmpty(email)) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "email"));
            }
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "email"));
            }

            String status = userExcel.getStatus();
            if (StringUtils.isEmpty(status)) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "status"));
            }
            if (!"0".equals(status) && !"1".equals(status)) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "status"));
            }

            String createdTime = userExcel.getCreatedTime();
            if (StringUtils.isEmpty(createdTime)) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "created time"));
            }
            try {
                DATE_TIME_FORMAT.parse(createdTime);
            } catch (ParseException e) {
                throw new GlobalException(CodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "created time"));
            }
        }
    }

    /**
     * userExcelList  transform userList
     *
     * @param userExcelList
     * @return
     */
    private List<User> userExcelList2UserList(List<UserExcel> userExcelList) throws ParseException {
        Date now = new Date();
        List<User> userList = new ArrayList<>();
        for (UserExcel userExcel : userExcelList) {
            User user = User.builder()
                    .username(userExcel.getUsername())
                    .password(userExcel.getPassword())
                    .nickname(userExcel.getNickname())
                    .email(userExcel.getEmail())
                    .avatar(userExcel.getAvatar())
                    .status(Integer.valueOf(userExcel.getStatus()))
                    .createdTime(DATE_TIME_FORMAT.parse(userExcel.getCreatedTime())).build();

            user.setCreatedBy("import");
            user.setUpdatedBy("import");
            user.setUpdatedTime(now);
            userList.add(user);
        }
        return userList;
    }

}
