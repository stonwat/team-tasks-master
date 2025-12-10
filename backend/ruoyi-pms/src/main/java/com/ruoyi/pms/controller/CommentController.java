package com.ruoyi.pms.controller;

import com.ruoyi.pms.common.ResponseResult;
import com.ruoyi.pms.dto.calendar.ExceptionDateListVm;
import com.ruoyi.pms.dto.calendar.WorkWeekSaveDm;
import com.ruoyi.pms.dto.comment.CommentListVm;
import com.ruoyi.pms.dto.comment.CommentSaveDm;
import com.ruoyi.pms.service.CommentService;
import com.ruoyi.pms.service.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pms/comment")
public class CommentController {

    @Autowired
    CommentService commentService;


    @PreAuthorize("@ss.hasPermi('pms:project:save')")
    @PostMapping("/saveComment")
    public ResponseResult<Void> saveComment(@RequestBody CommentSaveDm dm)  {
        commentService.saveComment(dm);
        return ResponseResult.success();
    }


    @PreAuthorize("@ss.hasPermi('pms:project:list')")
    @GetMapping("/listComment")
    public ResponseResult<List<CommentListVm>> listComment(@RequestParam("moduleId") String moduleId) {
        return ResponseResult.success(commentService.listComment(moduleId));
    }


    @PreAuthorize("@ss.hasPermi('pms:project:delete')")
    @PostMapping("/deleteComment")
    public ResponseResult<Void> deleteComment(@RequestBody List<String> ids) {
        commentService.deleteComment(ids);
        return ResponseResult.success();
    }


}
