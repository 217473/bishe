import { defineStore } from 'pinia';

//定义store:defineStore
export const userSotre = defineStore('userSotre', {
  state: () => {
    return {
      userId: '',
      nickName: '',
    };
  },
  getters: {
    getUserId(state) {
      return state.userId;
    },
    getNickName(state) {
      return state.nickName;
    },
  },
  actions: {
    setUserId(userId: string) {
      this.userId = userId;
    },
    setNickName(nickName: string) {
      this.nickName = nickName;
    },
  },
  persist: {
    enabled: true,
    strategies: [
      {
        storage: sessionStorage,
        paths: ['userId', 'nickName'],
      },
    ],
  },
});
