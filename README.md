📚 PathPrakash

Smart School Management Platform

PathPrakash is a multi-school School Management SaaS platform designed to bring school administration, teachers, parents, and students onto a single digital platform.

The project is being developed with a focus on **scalability, role-based access, multi-school architecture, security, and centralized school management**.

🚀 Project Overview

Managing school activities often requires multiple systems for administration, teachers, students, parents, attendance, academics, communication, and reports.

PathPrakash aims to provide a unified platform where every user gets access to the features relevant to their role.

The platform follows a **multi-tenant architecture**, where multiple schools can use the same system while their data remains logically separated using a `schoolId` based tenant structure.

Main User Roles

- 👑 Super Admin
- 🏫 School Admin
- 👨‍🏫 Teacher
- 👨‍👩‍👧 Parent
- 🎓 Student

Each role has its own permissions and dashboard.

---

# 🎯 Project Goals

The primary goals of PathPrakash are:

- Build a centralized school management platform
- Support multiple schools from a single SaaS system
- Provide role-based access control
- Keep school data isolated
- Simplify school administration
- Improve communication between teachers, parents, and students
- Provide a scalable Firebase-based backend
- Create a modern Android application
- Build a web-based administration system
- Maintain secure authentication and authorization

🏗️ System Architecture

PathPrakash follows a multi-school SaaS architecture.

                         ┌─────────────────────┐
                         │     Super Admin     │
                         └──────────┬──────────┘
                                    │
                         ┌──────────▼──────────┐
                         │   School Management │
                         └──────────┬──────────┘
                                    │
                 ┌──────────────────┼──────────────────┐
                 │                  │                  │
        ┌────────▼────────┐ ┌──────▼───────┐ ┌───────▼────────┐
        │   School A      │ │   School B   │ │    School C    │
        │   schoolId=A    │ │ schoolId=B   │ │    schoolId=C  │
        └────────┬────────┘ └──────┬───────┘ └───────┬────────┘
                 │                 │                 │
        ┌────────┼────────┐        │        ┌────────┼────────┐
        │        │        │        │        │        │        │
     Admin   Teachers  Students  Parents   Admin   Teachers  Students
