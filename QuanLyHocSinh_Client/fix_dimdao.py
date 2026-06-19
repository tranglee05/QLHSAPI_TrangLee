#!/usr/bin/env python3

file_path = r'D:\Documents\Nam 3 Ki 2\CongNghePhanMem\QuanLyHocSinh\src\main\java\Dao\DiemDAO.java'

with open(file_path, 'r', encoding='utf-8') as f:
    lines = f.readlines()

# Find and insert setTenMH after setMaMH (around line 31)
for i, line in enumerate(lines):
    if 'd.setMaMH(rs.getString("MaMH"));' in line and i < 35:
        # Insert setTenMH after this line
        lines.insert(i + 1, '                d.setTenMH(rs.getString("TenMH"));\n')
        break

with open(file_path, 'w', encoding='utf-8') as f:
    f.writelines(lines)

print("Fixed: Added setTenMH to DiemDAO")
