<template>
    <ContentFiled>
        <table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>玩家A</th>
                    <th>玩家B</th>
                    <th>对局结果</th>
                    <th>对局时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in records" :key="item.record.id">
                    <td>
                        <img :src="item.a_photo" alt="" class="record-user-photo">
                        <span class="record-username">{{ item.a_username }}</span>    
                    </td>
                    <td>
                        <img :src="item.b_photo" alt="" class="record-user-photo">
                        <span class="record-username">{{ item.b_username }}</span> 
                    </td>
                    <td>
                        {{ item.result }}
                    </td>
                    <td>
                        {{ item.record.createdTime }}
                    </td>
                    <td>
                        <button @click="open_record_content(item.record.id)" type="button" class="btn btn-outline-secondary" style="margin-right: 10px;">查看对局详情</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="pagesize-select" style="float: left; margin-left: 5%;">
            <span class="pagesize-select-text">请选择每页显示的个数</span>
            <select v-model="select_pagesize" class="form-select" aria-label="Default select example" @change="changePageSize()">
                <option value="5" selected>5</option>
                <option value="10">10</option>
                <option value="20">20</option>
            </select>
        </div>
        <nav aria-label="...">
            <ul class="pagination" style="float: right; margin-right: 5%;">
                <li class="page-item" @click="click_page(-2)">
                    <a class="page-link" href="#">&laquo;</a>
                </li>
                <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                    <a class="page-link" href="#">{{ page.number }}</a>
                </li>
                <li class="page-item" @click="click_page(-1)">
                    <a class="page-link" href="#">&raquo;</a>
                </li>
            </ul>
        </nav>
    </ContentFiled>
</template>

<script>
import ContentFiled from '../../components/ContentFiled.vue'
import $ from 'jquery'
import { ref } from 'vue'
import { useStore } from 'vuex'
import router from '@/router'

export default {
    components: {
        ContentFiled
    },

    setup() {
        const store = useStore();
        let records = ref([]);
        let current_page = 1;
        let total_record = 0;
        let pages = ref([]);
        let select_pagesize = ref("5");

        const changePageSize = () => {
            click_page(1);
        }

        const click_page = pageNumber => {
            if (pageNumber === -2) {
                pageNumber = current_page - 1;
            } else if (pageNumber === -1) {
                pageNumber = current_page + 1;
            } 
            let max_pages = parseInt(Math.ceil(total_record / select_pagesize.value));
            if (pageNumber >= 1 && pageNumber <= max_pages) {
                pull_page(pageNumber);
            }
        }

        const update_pages = () => {
            let max_pages = parseInt(Math.ceil(total_record / select_pagesize.value));
            let new_pages = [];
            for (let i = current_page - 2; i <= current_page + 2; i ++) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === current_page ? "active" : "",
                    });
                }
            } 
            console.log(new_pages);
            pages.value = new_pages;
        }

        const pull_page = pageNumber => {
            current_page = pageNumber;
            $.ajax({
                url: "http://127.0.0.1:3000/record/getList", 
                type: "get",
                data: {
                    pageNumber,
                    pageSize: select_pagesize.value,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    records.value = resp.records;
                    total_record = resp.total_record;
                    update_pages();
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }

        pull_page(current_page);

        const stringToMap = map => {
            let g = [];
            for (let i = 0, k = 0; i < 13; i ++) {
                let line = [];
                for (let j = 0; j < 14; j ++, k ++) {
                    if (map[k] === '0') {
                        line.push(0);
                    } else {
                        line.push(1);
                    }
                }
                g.push(line);
            }
            return g;
        }

        const open_record_content = recordId => {
            for (const item of records.value) {
                if (item.record.id === recordId) {
                    let record = item.record;
                    store.commit("updateIsRecord", true);
                    store.commit("updateGame", {
                        map: stringToMap(record.map),
                        a_id: record.aid,
                        a_sx: record.asx,
                        a_sy: record.asy,
                        b_id: record.bid,
                        b_sx: record.asx,
                        b_sy: record.bsy,
                    });
                    store.commit("updateSteps", {
                        a_steps: record.asteps,
                        b_steps: record.bsteps,
                    });
                    store.commit("updateRecordLoser", record.loser);
                    router.push({
                        name: "record_content",
                        params: {
                            recordId,
                        }
                    });
                    break;
                }
            }
        }

        return {
            records,
            open_record_content,
            pages,
            click_page,
            changePageSize,
            select_pagesize,
        }
    }
}
</script>

<style scoped>
    img.record-user-photo {
        width: 4vh;
        border-radius: 50%;
    }
    span.record-username {
        font-size: 15px;
        margin-left: 5%;
    }

</style>