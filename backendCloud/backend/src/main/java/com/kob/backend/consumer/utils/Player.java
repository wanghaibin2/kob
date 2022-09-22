package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author :王冰冰
 * @date : 2022/9/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    public boolean check_tail_increasing(int step) {
        // 检测当前蛇尾的长度是否增加
        if (step <= 10) {
            return true;
        }
        return step % 3 == 1;
    }

    public List<Cell> getCells() {
        List<Cell> cellList = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy ={0, 1, 0, -1};
        int x = sx, y = sy, step = 0;
        cellList.add(new Cell(x, y));
        if (this.steps.size() != 0) {
            for (Integer it : steps) {
                x = x + dx[it];
                y = y + dy[it];
                cellList.add(new Cell(x, y));
                if (!check_tail_increasing(step ++)) {
                    cellList.remove(0);
                }
            }
        }
        return cellList;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int it : steps) {
            res.append(it);
        }
        return res.toString();
    }
}
