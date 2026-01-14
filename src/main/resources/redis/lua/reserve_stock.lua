-- KEYS: danh sách các stockKey (KEYS[1], KEYS[2]...)
-- ARGV: danh sách quantity tương ứng (ARGV[1], ARGV[2]...)

-- Bước 1: Kiểm tra tất cả xem có đủ hàng không
for i = 1, #KEYS do
    local stock = tonumber(redis.call("GET", KEYS[i]))
    if stock == nil or stock < tonumber(ARGV[i]) then
        return -1 -- Trả về lỗi ngay nếu có 1 món hết hàng
    end
end

-- Bước 2: Nếu tất cả đều đủ, mới thực hiện trừ
for i = 1, #KEYS do
    redis.call("DECRBY", KEYS[i], ARGV[i])
end

return 1 -- Thành công cho cả đơn hàng