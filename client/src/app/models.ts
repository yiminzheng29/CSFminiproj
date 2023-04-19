export interface News {
    newsId?: string
    sourceName: string
    author: string
    title: string
    description: string
    url: string
    urlImage: string
    publishedAt: Date
    content: string
    likes?: number
    liked?: boolean
    toggle?: string
}

export interface Issue {
    issueNo: number
    title: string
    description: string
    priority: 'low' | 'high'
    type: 'Feature' | 'Bug' | 'Documentation'
    completed?: Date
}

// export interface User {
//     username?: string
//     password?: string
//     firstname?: string
//     lastname?: string
//     dob?: Date
//     email?: string
//     phone?: string
//     token?: string
// }

export interface User {
    username: string
    password: string
    firstname: string
    lastname: string
    email: string
    profileImage: File
    token: string
    profileImageUrl: string
}

export interface CanLeave {
    canLeave(): boolean
}

export interface Response {
    newsId: string
    username: string
}

export interface comments {
    commentId?: string
    username: string
    firstname: string
    lastname: string
    comment: string
    publishedAt: Date
    newsId?: string
}