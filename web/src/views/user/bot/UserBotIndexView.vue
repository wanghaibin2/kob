<template>
    <ContentFiled>My Bot</ContentFiled>
</template>

<script>
    import ContentFiled from '../../../components/ContentFiled.vue'
    import $ from 'jquery'
    import { useStore } from 'vuex'

    export default {
        components: {
            ContentFiled
        },
        setup() {
            const store = useStore();
            // $.ajax({
            //     url: "http://127.0.0.1:3000/user/bot/add",
            //     type: "post",
            //     data: {
            //         title: "Bot的title",
            //         description: "Bot的description",
            //         context: "Bot的context",
            //     },
            //     headers: {
            //         Authorization: "Bearer " + store.state.user.token,
            //     },
            //     success(resp) {
            //         console.log(resp);
            //     },
            //     error(resp) {
            //         console.log(resp);
            //     }
            // })

            // $.ajax({
            //     url: "http://127.0.0.1:3000/user/bot/remove",
            //     type: "post",
            //     data: {
            //         botId: 3,
            //     },
            //     headers: {
            //         Authorization: "Bearer " + store.state.user.token,
            //     },
            //     success(resp) {
            //         console.log(resp);
            //     },
            //     error(resp) {
            //         console.log(resp);
            //     }
            // })
            
            // $.ajax({
            //     url: "http://127.0.0.1:3000/user/bot/update",
            //     type: "post",
            //     data: {
            //         botId: 2,
            //         title: "Bot的title002",
            //         description: "Bot的description002",
            //         context: "Bot的context002",
            //     },
            //     headers: {
            //         Authorization: "Bearer " + store.state.user.token,
            //     },
            //     success(resp) {
            //         console.log(resp);
            //     },
            //     error(resp) {
            //         console.log(resp);
            //     }
            // })

            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/search",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    console.log(resp);
                },
                error(resp) {
                    console.log(resp);
                }
            })

        }
    }
</script>

<style scoped>

</style>