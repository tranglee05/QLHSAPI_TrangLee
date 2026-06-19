#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script tạo icons PNG từ emoji
Cần: pip install Pillow
"""

from PIL import Image, ImageDraw
import os

# Đảm bảo thư mục tồn tại
os.makedirs("src/main/resources/icons", exist_ok=True)

# Danh sách icons cần tạo
ICONS = {
    "logout.png": ("🚪", "Đăng xuất"),
    "exit.png": ("❌", "Thoát"),
    "class.png": ("📚", "Lớp học"),
    "teacher.png": ("👨‍🏫", "Giáo viên"),
    "group.png": ("👥", "Nhóm"),
    "subject.png": ("📖", "Môn học"),
    "schedule.png": ("📅", "Thời khóa biểu"),
    "room.png": ("🏫", "Phòng học"),
    "score.png": ("📊", "Điểm số"),
    "conduct.png": ("⭐", "Hạnh kiểm"),
    "exam.png": ("📝", "Lịch thi"),
    "fee.png": ("💰", "Học phí"),
    "notification.png": ("📢", "Thông báo"),
    "review.png": ("⚖️", "Phúc khảo"),
    "student.png": ("👤", "Học sinh"),
    "user.png": ("🔐", "Tài khoản"),
    "policy.png": ("📋", "Chính sách"),
}

print("🎨 Tạo icons từ emoji...")
print("-" * 50)

try:
    import sys
    # Kiểm tra Python version
    if sys.version_info < (3, 6):
        print("❌ Yêu cầu Python 3.6+")
        sys.exit(1)

    from PIL import ImageFont
    
    size = 32
    created = 0
    
    for filename, (emoji, description) in ICONS.items():
        try:
            # Tạo ảnh transparent
            img = Image.new("RGBA", (size, size), color=(255, 255, 255, 0))
            draw = ImageDraw.Draw(img)
            
            # Vẽ emoji vào giữa
            # Sử dụng font mặc định (hoặc có thể dùng font tùy chỉnh)
            draw.text((3, 2), emoji, fill=(0, 0, 0, 255))
            
            # Lưu file
            filepath = f"src/main/resources/icons/{filename}"
            img.save(filepath, "PNG")
            
            print(f"✓ {filename:25} <- {emoji} ({description})")
            created += 1
            
        except Exception as e:
            print(f"✗ {filename:25} ERROR: {str(e)[:40]}")
    
    print("-" * 50)
    print(f"✓ Thành công: {created}/{len(ICONS)} icons")
    
    if created == len(ICONS):
        print("\n✨ Hoàn tất! Icons đã sẵn sàng cho MainFormNew.java")
        print("📁 Vị trí: src/main/resources/icons/")
    
except ImportError:
    print("❌ Lỗi: Cần cài PIL/Pillow")
    print("   Chạy: pip install Pillow")
except Exception as e:
    print(f"❌ Lỗi chung: {e}")
