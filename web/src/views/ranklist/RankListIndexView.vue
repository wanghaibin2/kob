<template>
    <ContentFiled><table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>用户头像</th>
                    <th>用户姓名</th>
                    <th>用户rating</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in users" :key="item.id">
                    <td>
                        <img :src="item.photo" alt="" class="user-photo"> 
                    </td>
                    <td>
                        <span class="username">{{ item.userName }}</span>
                    </td>
                    <td>
                        {{ item.rating }}
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="pagesize-select" style="float: left; margin-left: 5%;">
            <span class="pagesize-select-text">请选择每页显示的个数</span>
            <select v-model="select_pagesize" class="form-select" aria-label="Default select example" @change="changePageSize()">
                <option value="3" selected>3</option>
                <option value="5">5</option>
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

    export default {
        components: {
            ContentFiled
        },

        setup() {
            const store = useStore();
            let users = ref([]);
            let current_page = 1;
            let total_user = 0;
            let pages = ref([]);
            let select_pagesize = ref("3");

            const changePageSize = () => {
                click_page(1);
            }

            const click_page = pageNumber => {
                if (pageNumber === -2) {
                    pageNumber = current_page - 1;
                } else if (pageNumber === -1) {
                    pageNumber = current_page + 1;
                } 
                let max_pages = parseInt(Math.ceil(total_user / select_pagesize.value));
                if (pageNumber >= 1 && pageNumber <= max_pages) {
                    pull_page(pageNumber);
                }
            }

            const update_pages = () => {
                let max_pages = parseInt(Math.ceil(total_user / select_pagesize.value));
                let new_pages = [];
                for (let i = current_page - 2; i <= current_page + 2; i ++) {
                    if (i >= 1 && i <= max_pages) {
                        new_pages.push({
                            number: i,
                            is_active: i === current_page ? "active" : "",
                        });
                    }
                } 
                pages.value = new_pages;
            }

            const pull_page = (pageNumber) => {
                current_page = pageNumber;
                $.ajax({
                    url: "http://127.0.0.1:3000/rank/getList", 
                    type: "get",
                    data: {
                        pageNumber,
                        pageSize: select_pagesize.value,
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp) {
                        users.value = resp.users;
                        total_user = resp.total_user;
                        update_pages();
                    },
                    error(resp) {
                        console.log(resp);
                    }
                })
            }

            pull_page(current_page);

            return {
                users,
                pages,
                click_page,
                changePageSize,
                select_pagesize,
            }
        }
    }
</script>

<style scoped>
    img.user-photo {
        width: 4vh;
        border-radius: 50%;
    }
    span.username {
        font-size: 15px;
    }
    div.pagesize-select > select {
        width: 60%;
    }
    span.pagesize-select-text {
        font-size: 16px;
    }
</style>