import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snack extends AcGameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells= [new Cell(info.r, info.c)]; // 存放蛇的身体 cells[0]存放蛇头
        this.next_cell = null; // 下一步的目标位置

        this.speed = 5; // 蛇每一秒走1个格子

        this.direction = -1; //-1表示没有指令, 0 1 2 3表示上右下左
        this.status = "idle"; // idle静止, move正在移动, die死亡

        this.dr = [-1, 0, 1, 0]; 
        this.dc = [0, 1, 0, -1];

        this.step = 0; // 表示回合数
        this.eps = 1e-2; // 允许的误差
    }

    start() {

    }

    set_direction(d) {
        this.direction = d;
    }

    check_tail_increasing() {
        // 检测当前蛇尾的长度是否增加
        if (this.step <= 10) {
            return true;
        }
        if (this.step % 3 === 1) {
            return true;
        }
        return false;
    }

    next_step() { // 更新蛇的状态
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1 // 清空操作
        this.status = "move";
        this.step ++;

        const k = this.cells.length;
        for (let i = k; i > 0; i --) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
    }

    update_move() { 
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < this.eps) {
            this.cells[0] = this.next_cell; // 添加一个新蛇头
            this.next_cell = null;
            this.status = "idle";

            if (!this.check_tail_increasing()) {
                this.cells.pop();
            }
        } else {
            const move_distance = this.speed * this.timedelta / 1000; // 每两帧走的距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;
            
            if (!this.check_tail_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }
    
    update() { // 一帧一次
        if (this.status === 'move') {
            this.update_move();
        }
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2, 0, Math.PI * 2);
            ctx.fill();
        }
    }
} 